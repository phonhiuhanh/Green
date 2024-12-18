package com.poly.greeen.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class GiaodienController {
    @GetMapping("/shop")
    public String Home() {
        return "/giaodien/shop";
    }

    @GetMapping("/products")
    public String product() {
        return "/giaodien/product";
    }

    @GetMapping("/order")
    public String order() {
        return "/giaodien/order";
    }

    @GetMapping("/details")
    public String details() {
        return "/giaodien/details";
    }

    @GetMapping("/login")
    public String login() {
        return "/giaodien/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/giaodien/register";
    }

    @GetMapping("/search")
    public String toSeach() {
        return "/giaodien/search";
    }

    @GetMapping("/forgot-password")
    public String toForgotPassword() {
        return "/giaodien/forgotPassword";
    }

    @GetMapping("/p/test")
    public String toTest() {
        return "/giaodien/test/index";
    }
}
