package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class Login {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "giaodien/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("phone") String phone,
                        @RequestParam("password") String password,
                        Model model) {
        Optional<Customer> customer = customerRepository.findByPhone(phone);
        if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
            return "redirect:/giaodien/shop";
        } else {
            model.addAttribute("error", "Số điện thoại hoặc mật khẩu không chính xác.");
            return "giaodien/login";
        }
    }
}
