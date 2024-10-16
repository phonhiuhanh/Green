package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Product;
import com.poly.greeen.Repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        return "admin/nhanvien";
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
        return "admin/donhang";
    }

}
