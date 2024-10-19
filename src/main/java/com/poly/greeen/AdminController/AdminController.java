package com.poly.greeen.AdminController;

import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping("/trangchu")
    public String HomePage() {
        return "/admin/index";
    }

    @RequestMapping("/nhanvien")
    public String AddEmployeePage() {
        return "customer";
    }

    @RequestMapping("/chamcong")
    public String ViewEmployeeDeltailPage() {
        return "admin/chamcong";
    }

    @RequestMapping("/chucvu")
    public String ManageRolePage() {
        return "admin/chucvu";
    }
    @RequestMapping("/donhang")
    public String Managedonhang() {
        return "oder";
    }

}
