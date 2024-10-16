package com.poly.greeen.API;

import com.poly.greeen.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/role")
@Controller
public class Role {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/nhanvien")
    public String getEmployees(Model model) {
        List<com.poly.greeen.Entity.Role> employees = roleRepository.findAll(); // Lấy tất cả vai trò
        model.addAttribute("employees", employees);
        return "/admin/nhanvien";
    }
}
