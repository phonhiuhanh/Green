package com.poly.greeen;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/index")
    public String hehe(Model model) {
        return "giaodien/index";
    }
}
