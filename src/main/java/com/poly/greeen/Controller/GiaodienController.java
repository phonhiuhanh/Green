package com.poly.greeen.Controller;
import com.poly.greeen.Entity.Product;
import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/index")
public class GiaodienController {
    @RequestMapping("/shop")
    public String Home() {
        return "/giaodien/shop";
    }

    @RequestMapping("/products")
    public String product() {
        return "/giaodien/product";
    }

    @RequestMapping("/oder")
    public String oder() {
        return "/giaodien/oder";
    }

    @RequestMapping("/details")
    public String details() {
        return "/giaodien/details";
    }

    @RequestMapping("/login")
    public String login() {
        return "/giaodien/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "/giaodien/register";
    }
}
