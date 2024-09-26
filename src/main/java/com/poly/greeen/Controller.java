package com.poly.greeen;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/index")
    public String hehe(Model model) {
        return "giaodien/index";
    }

    @GetMapping("/nhanvien")
    public String employee(Model model) {
        model.addAttribute("message", "Chào mừng đến trang nhân viên!");
        return "/QLnhanvien/nhanvien"; // Tên file HTML của trang nhân viên
    }

    @GetMapping("/khachhang")
    public String customer(Model model) {
        model.addAttribute("message", "Chào mừng đến trang khách hàng!");
        return "QLkhachhang/khachhang"; // Tên file HTML của trang khách hàng
    }

    @GetMapping("/donhang")
    public String order(Model model) {
        model.addAttribute("message", "Chào mừng đến trang đơn hàng!");
        return "QLdonhang/donhang"; // Tên file HTML của trang đơn hàng
    }
}



