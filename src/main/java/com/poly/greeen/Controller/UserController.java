package com.poly.greeen.Controller;

import com.poly.greeen.Data.RegisterRequest;
import com.poly.greeen.Entity.Users;
import com.poly.greeen.Repository.UsersRepository;
import com.poly.greeen.Security.CustomUserDetails;
import com.poly.greeen.Security.ServerUriProvider;
import com.poly.greeen.Utils.AuthorizationCodeGenerator;
import com.poly.greeen.Utils.EmailService;
import com.poly.greeen.Utils.SystemStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UsersRepository userRepository;
    private final SystemStorage systemStorage;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            log.info("Yêu cầu REST để lấy người dùng hiện tại");
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var user = userRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/p/forgot-password/request")
    public ResponseEntity<?> requestForgotPassword(@RequestParam String email) {
        log.info("Yêu cầu REST để quên mật khẩu với email: {}", email);
        try {
            var user = userRepository.findByEmailOrPhone(email, email)
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            String otp = AuthorizationCodeGenerator.generateRandomOTP();
            var registerRequest = RegisterRequest.builder()
                    .user(user)
                    .code(otp)
                    .createdAt(Timestamp.from(Instant.now()))
                    .build();
            systemStorage.put(email + "-forgot-password", registerRequest);
            // Compose the email
            String subject = "Đặt lại mật khẩu";
            String message = "Mã OTP xác nhận đặt lại mật khẩu: " + otp + "<br>" +
                    "<span style=\"color: red;\">Mã OTP này chỉ có hiệu lực trong 5 phút.</span>";

            // Send the email
            emailService.sendEmail(email, subject, message);
            return ResponseEntity.ok().body("Yêu cầu đặt lại mật khẩu đã được gửi đến email của bạn");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

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
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            systemStorage.remove(email + "-forgot-password");
            return ResponseEntity.ok().body("Đặt lại mật khẩu thành công, bạn có thể đóng trang này và đăng nhập ngay bây giờ");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Users user) {
        log.info("Yêu cầu REST để cập nhật thông tin người dùng: {}", user);
        try {
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var currentUser = userRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            currentUser.setUsername(user.getUsername());
            currentUser.setAddress(user.getAddress());
            currentUser.setCity(user.getCity());
            userRepository.save(currentUser);
            return ResponseEntity.ok().body("Cập nhật thông tin thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        log.info("Yêu cầu REST để cập nhật mật khẩu");
        try {
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var currentUser = userRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy người dùng"));
            if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không chính xác");
            }
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
            return ResponseEntity.ok().body("Cập nhật mật khẩu thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/manager")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        log.info("REST request to create user: {}", user);
        user.setUserID(userRepository.getNextUserId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);
        System.out.println(savedUser);
        return ResponseEntity.ok(savedUser);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        log.info("REST request to get all users");
        List<Users> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        log.info("REST request to get user by ID: {}", id);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    // Update a user
    @PutMapping("/manager/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users userDetails) {
        log.info("REST request to update user: {}", userDetails);
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        user.setCity(userDetails.getCity());
        user.setRole(userDetails.getRole());
        // Update other fields as necessary

        Users updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user
    @DeleteMapping("/manager/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("REST request to delete user by ID: {}", id);
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}