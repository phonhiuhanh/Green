package com.poly.greeen.AdminController;

import com.poly.greeen.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
public class Users {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/list")
    public String listUsers(Model model) {
        List<com.poly.greeen.Entity.Users> userList = usersRepository.findAll();
        model.addAttribute("users", userList);
        return "admin/chucvu"; // Tên view mà bạn muốn trả về
    }
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Integer userID) {
        usersRepository.deleteById(userID);
        return "redirect:/chucvu"; // Chuyển hướng về danh sách người dùng
    }
}
