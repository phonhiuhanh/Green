package com.poly.greeen.Controller;

import com.poly.greeen.Repository.CustomerRepository;
import com.poly.greeen.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/get-current-customer")
    public ResponseEntity<?> getCurrentCustomer() {
        try {
            log.info("REST request to get current customer");
            var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var customer = customerRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                    .orElseThrow(() -> new Exception("Customer not found"));
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
