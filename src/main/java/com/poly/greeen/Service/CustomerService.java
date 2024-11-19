package com.poly.greeen.Service;

import com.poly.greeen.Data.RegisterRequest;
import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Repository.CustomerRepository;
import com.poly.greeen.Security.CustomUserDetails;
import com.poly.greeen.Security.ServerUriProvider;
import com.poly.greeen.Utils.AuthorizationCodeGenerator;
import com.poly.greeen.Utils.EmailService;
import com.poly.greeen.Utils.SystemStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SystemStorage systemStorage;
    private final EmailService emailService;
    private final ServerUriProvider serverUriProvider;
    private final PasswordEncoder passwordEncoder;

    public Customer getCurrentCustomer() throws Exception {
        var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
    }

    public void register(Customer customer) throws Exception {
        String code = AuthorizationCodeGenerator.generateAuthorizationCode();
        customer.setCustomerID(customerRepository.getNextCustomerId());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        var registerRequest = RegisterRequest.builder()
                .customer(customer)
                .code(code)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        systemStorage.put(customer.getEmail() + "-register", registerRequest);
        String subject = "Xác nhận tài khoản của bạn";
        String confirmationLink = serverUriProvider.getServerUri() + "/api/customers/p/verify-account?email=" + customer.getEmail() + "&code=" + code;
        String message = "Vui lòng nhấp vào liên kết dưới đây để xác nhận đăng ký của bạn:<br>" +
                "<a href=\"" + confirmationLink + "\">" + "Link xác nhận" + "</a><br>" +
                "<span style=\"color: red;\">Liên kết này chỉ có hiệu lực trong 5 phút.</span>";
        emailService.sendEmail(customer.getEmail(), subject, message);
    }

    public void verifyAccount(String email, String code) throws Exception {
        var registerRequest = (RegisterRequest) systemStorage.get(email + "-register");
        if (registerRequest == null) {
            throw new Exception("Email không hợp lệ");
        }
        if (!registerRequest.getCode().equals(code)) {
            throw new Exception("Mã không hợp lệ");
        }
        long currentTime = Instant.now().toEpochMilli();
        long createdTime = registerRequest.getCreatedAt().getTime();
        if (currentTime - createdTime > 5 * 60 * 1000) {
            systemStorage.remove(email + "-register");
            throw new Exception("Liên kết đã hết hạn");
        }
        var customer = registerRequest.getCustomer();
        customerRepository.save(customer);
        systemStorage.remove(email + "-register");
    }

    public void requestForgotPassword(String email) throws Exception {
        var customer = customerRepository.findByEmailOrPhone(email, email)
                .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
        String otp = AuthorizationCodeGenerator.generateRandomOTP();
        var registerRequest = RegisterRequest.builder()
                .customer(customer)
                .code(otp)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        systemStorage.put(email + "-forgot-password", registerRequest);
        String subject = "Đặt lại mật khẩu";
        String message = "Mã OTP xác nhận đặt lại mật khẩu: " + otp + "<br>" +
                "<span style=\"color: red;\">Mã OTP này chỉ có hiệu lực trong 5 phút.</span>";
        emailService.sendEmail(email, subject, message);
    }

    public void verifyForgotPassword(String email, String otp, String newPassword) throws Exception {
        var registerRequest = (RegisterRequest) systemStorage.get(email + "-forgot-password");
        if (registerRequest == null) {
            throw new Exception("Email không hợp lệ");
        }
        if (!registerRequest.getCode().equals(otp)) {
            throw new Exception("Mã OTP không hợp lệ");
        }
        long currentTime = Instant.now().toEpochMilli();
        long createdTime = registerRequest.getCreatedAt().getTime();
        if (currentTime - createdTime > 5 * 60 * 1000) {
            systemStorage.remove(email + "-forgot-password");
            throw new Exception("Mã OTP đã hết hạn");
        }
        var customer = registerRequest.getCustomer();
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        systemStorage.remove(email + "-forgot-password");
    }

    public void updateProfile(Customer customer) throws Exception {
        var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var currentCustomer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
        currentCustomer.setUsername(customer.getUsername());
        currentCustomer.setAddress(customer.getAddress());
        currentCustomer.setCity(customer.getCity());
        customerRepository.save(currentCustomer);
    }

    public void updatePassword(String oldPassword, String newPassword) throws Exception {
        var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var currentCustomer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new Exception("Không tìm thấy khách hàng"));
        if (!passwordEncoder.matches(oldPassword, currentCustomer.getPassword())) {
            throw new Exception("Mật khẩu cũ không chính xác");
        }
        currentCustomer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(currentCustomer);
    }
}