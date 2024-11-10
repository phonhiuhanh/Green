package com.poly.greeen.View;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @RequestMapping("/index")
    public String index() {
        return "/admin/index";
    }

    @RequestMapping("/product")
    public String product() {
        return "/admin/products";
    }

    @RequestMapping("/order")
    public String order() {
        return "/admin/order";
    }

    @RequestMapping("/delivery")
    public String delivery() {
        return "/admin/delivery";
    }

    @RequestMapping("/customer")
    public String customer() {
        return "/admin/customer";
    }

    @RequestMapping("/import")
    public String importInfo() {
        return "/admin/Import";
    }

    @RequestMapping("/staff")
    public String staff() {
        return "/admin/chucvu";
    }

    @RequestMapping("/attendance")
    public String attendance() {
        return "/admin/chamcong";
    }
}
