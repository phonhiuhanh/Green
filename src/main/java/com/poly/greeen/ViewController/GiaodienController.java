package com.poly.greeen.ViewController;

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

    @GetMapping("/oder")
    public String oder() {
        return "/giaodien/oder";
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
}
