package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Users;
import com.poly.greeen.Service.UserService;
import com.poly.greeen.Utils.EmailService;
import com.poly.greeen.Utils.SystemStorage;
import com.poly.greeen.Data.RegisterRequest;
import com.poly.greeen.Security.CustomUserDetails;
import com.poly.greeen.Utils.AuthorizationCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;
    private final SystemStorage systemStorage;
    private final EmailService emailService;

    // Lấy người dùng hiện tại
    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            log.info("Yêu cầu REST để lấy người dùng hiện tại");
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var user = userService.getCurrentUser(userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    // Yêu cầu quên mật khẩu
    @PostMapping("/p/forgot-password/request")
    public ResponseEntity<?> requestForgotPassword(@RequestParam String email) {
        log.info("Yêu cầu REST để quên mật khẩu với email: {}", email);
        try {
            var user = userService.findByEmailOrPhone(email)
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            String otp = AuthorizationCodeGenerator.generateRandomOTP();
            var registerRequest = RegisterRequest.builder()
                    .user(user)
                    .code(otp)
                    .createdAt(Timestamp.from(Instant.now()))
                    .build();
            systemStorage.put(email + "-forgot-password", registerRequest);
            String subject = "Đặt lại mật khẩu";
            String message = "Mã OTP xác nhận đặt lại mật khẩu: " + otp + "<br>" +
                    "<span style=\"color: red;\">Mã OTP này chỉ có hiệu lực trong 5 phút.</span>";
            emailService.sendEmail(email, subject, message);
            return ResponseEntity.ok().body("Yêu cầu đặt lại mật khẩu đã được gửi đến email của bạn");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    // Xác minh quên mật khẩu
    @PostMapping("/p/forgot-password/verify")
    public ResponseEntity<?> verifyForgotPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        log.info("Yêu cầu REST để xác minh quên mật khẩu với email: {} và mã OTP: {}", email, otp);
        try {
            var registerRequest = (RegisterRequest) systemStorage.get(email + "-forgot-password");
            if (registerRequest == null) {
                return ResponseEntity.badRequest().body("Email không hợp lệ");
            }
            if (!registerRequest.getCode().equals(otp)) {
                return ResponseEntity.badRequest().body("Mã OTP không hợp lệ");
            }
            long currentTime = Instant.now().toEpochMilli();
            long createdTime = registerRequest.getCreatedAt().getTime();
            if (currentTime - createdTime > 5 * 60 * 1000) {
                systemStorage.remove(email + "-forgot-password");
                return ResponseEntity.badRequest().body("Mã OTP đã hết hạn");
            }
            var user = registerRequest.getUser();
            userService.updatePassword(user, newPassword);
            systemStorage.remove(email + "-forgot-password");
            return ResponseEntity.ok().body("Đặt lại mật khẩu thành công, bạn có thể đóng trang này và đăng nhập ngay bây giờ");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    // Cập nhật thông tin người dùng
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Users user) {
        log.info("Yêu cầu REST để cập nhật thông tin người dùng: {}", user);
        try {
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var currentUser = userService.getCurrentUser(userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            currentUser.setUsername(user.getUsername());
            currentUser.setAddress(user.getAddress());
            currentUser.setCity(user.getCity());
            userService.updateUser(currentUser.getUserID(), currentUser);
            return ResponseEntity.ok().body("Cập nhật thông tin thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    // Cập nhật mật khẩu người dùng
    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        log.info("Yêu cầu REST để cập nhật mật khẩu");
        try {
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var currentUser = userService.getCurrentUser(userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            if (!userService.passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không chính xác");
            }
            userService.updatePassword(currentUser, newPassword);
            return ResponseEntity.ok().body("Cập nhật mật khẩu thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    // Tạo mới người dùng
    @PostMapping("/manager")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        log.info("Yêu cầu REST để tạo người dùng: {}", user);
        Users savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Lấy tất cả người dùng
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        log.info("Yêu cầu REST để lấy tất cả người dùng");
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Lấy người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        log.info("Yêu cầu REST để lấy người dùng theo ID: {}", id);
        Users user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return ResponseEntity.ok(user);
    }

    // Cập nhật người dùng
    @PutMapping("/manager/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users userDetails) {
        log.info("Yêu cầu REST để cập nhật người dùng: {}", userDetails);
        Users updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa người dùng theo ID
    @DeleteMapping("/manager/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("Yêu cầu REST để xóa người dùng theo ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}