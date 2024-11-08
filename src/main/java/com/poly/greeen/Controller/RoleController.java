package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Role;
import com.poly.greeen.Service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@RestController
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            log.info("Yêu cầu REST để lấy tất cả các vai trò");
            List<Role> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}