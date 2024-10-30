package com.poly.greeen.Security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/get-username")
    public ResponseEntity<?> getUsername() {
        var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getAuthUser().getName());
    }

    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String email, @RequestParam String code, @RequestParam Timestamp timestamp) {
        return ResponseEntity.ok("Account verified");
    }
}
