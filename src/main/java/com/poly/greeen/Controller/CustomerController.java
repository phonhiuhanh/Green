package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get-current-customer")
    public ResponseEntity<?> getCurrentCustomer() {
        try {
            log.info("Yêu cầu REST để lấy khách hàng hiện tại");
            var customer = customerService.getCurrentCustomer();
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
            customerService.register(customer);
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
            customerService.verifyAccount(email, code);
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
            customerService.requestForgotPassword(email);
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
            customerService.verifyForgotPassword(email, otp, newPassword);
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
            customerService.updateProfile(customer);
            return ResponseEntity.ok().body("Cập nhật thông tin thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        log.info("Yêu cầu REST để cập nhật m���t khẩu");
        try {
            customerService.updatePassword(oldPassword, newPassword);
            return ResponseEntity.ok().body("Cập nhật mật khẩu thành công");
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}