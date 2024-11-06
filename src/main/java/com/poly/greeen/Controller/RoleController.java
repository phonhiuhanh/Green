package com.poly.greeen.Controller;

import com.poly.greeen.Data.RegisterRequest;
import com.poly.greeen.Entity.Role;
import com.poly.greeen.Entity.Users;
import com.poly.greeen.Repository.RoleRepository;
import com.poly.greeen.Repository.UsersRepository;
import com.poly.greeen.Security.CustomUserDetails;
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
@RequestMapping("/api/roles")
@RestController
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            log.info("Yêu cầu REST để lấy tất cả các vai trò");
            List<Role> roles = roleRepository.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}