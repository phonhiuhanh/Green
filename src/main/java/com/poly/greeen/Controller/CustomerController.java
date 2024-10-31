package com.poly.greeen.Controller;

import com.poly.greeen.Data.RegisterRequest;
import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Repository.CustomerRepository;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final SystemStorage systemStorage;
    private final EmailService emailService;
    private final ServerUriProvider serverUriProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/get-current-customer")
    public ResponseEntity<?> getCurrentCustomer() {
        try {
            log.info("Yêu cầu REST để lấy khách hàng hiện tại");
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var customer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/p/register")
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        log.info("Yêu cầu REST để đăng ký khách hàng: {}", customer);
        try {
            String code = AuthorizationCodeGenerator.generateAuthorizationCode();
            customer.setCustomerID(customerRepository.getNextCustomerId());
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            System.out.println(customer.getCustomerID());
            var registerRequest = RegisterRequest.builder()
                    .customer(customer)
                    .code(code)
                    .createdAt(Timestamp.from(Instant.now()))
                    .build();
            systemStorage.put(customer.getEmail() + "-register", registerRequest);
            // Compose the email
            String subject = "Xác nhận tài khoản của bạn";
            String confirmationLink = serverUriProvider.getServerUri() + "/api/customers/p/verify-account?email=" + customer.getEmail() + "&code=" + code;
            String message = "Vui lòng nhấp vào liên kết dưới đây để xác nhận đăng ký của bạn:<br>" +
                    "<a href=\"" + confirmationLink + "\">" + "Link xác nhận" + "</a><br>" +
                    "<span style=\"color: red;\">Liên kết này chỉ có hiệu lực trong 5 phút.</span>";

            // Send the email
            emailService.sendEmail(customer.getEmail(), subject, message);
            return ResponseEntity.ok().body("Đăng ký thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping("/p/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String email, @RequestParam String code) {
        log.info("Yêu cầu REST để xác minh tài khoản với email: {} và mã: {}", email, code);
        try {
            var registerRequest = (RegisterRequest) systemStorage.get(email + "-register");
            if (registerRequest == null) {
                return ResponseEntity.badRequest().body("Email không hợp lệ");
            }
            if (!registerRequest.getCode().equals(code)) {
                return ResponseEntity.badRequest().body("Mã không hợp lệ");
            }
            long currentTime = Instant.now().toEpochMilli();
            long createdTime = registerRequest.getCreatedAt().getTime();
            if (currentTime - createdTime > 5 * 60 * 1000) {
                systemStorage.remove(email + "-register");
                return ResponseEntity.badRequest().body("Liên kết đã hết hạn");
            }
            var customer = registerRequest.getCustomer();
            customerRepository.save(customer);
            systemStorage.remove(email + "-register");
            return ResponseEntity.ok().body("Xác minh tài khoản thành công, bạn có thể đóng trang này và đăng nhập ngay bây giờ");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/p/forgot-password/request")
    public ResponseEntity<?> requestForgotPassword(@RequestParam String email) {
        log.info("Yêu cầu REST để quên mật khẩu với email: {}", email);
        try {
            var customer = customerRepository.findByEmailOrPhone(email, email)
                    .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
            String otp = AuthorizationCodeGenerator.generateRandomOTP();
            var registerRequest = RegisterRequest.builder()
                    .customer(customer)
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
            var customer = registerRequest.getCustomer();
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            systemStorage.remove(email + "-forgot-password");
            return ResponseEntity.ok().body("Đặt lại mật khẩu thành công, bạn có thể đóng trang này và đăng nhập ngay bây giờ");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Customer customer) {
        log.info("Yêu cầu REST để cập nhật thông tin khách hàng: {}", customer);
        try {
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var currentCustomer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
            currentCustomer.setUsername(customer.getUsername());
            currentCustomer.setAddress(customer.getAddress());
            currentCustomer.setCity(customer.getCity());
            customerRepository.save(currentCustomer);
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
            var currentCustomer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
            if (!passwordEncoder.matches(oldPassword, currentCustomer.getPassword())) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không chính xác");
            }
            currentCustomer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(currentCustomer);
            return ResponseEntity.ok().body("Cập nhật mật khẩu thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}