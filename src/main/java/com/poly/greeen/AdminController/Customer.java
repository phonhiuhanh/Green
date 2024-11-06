package com.poly.greeen.AdminController;

import com.poly.greeen.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class Customer {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<com.poly.greeen.Entity.Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "admin/customer"; // Trả về view 'customerList.html'
    }

    @PostMapping("/manager/delete")
    public String deleteCustomer(@RequestParam("id") Integer id) {
        customerRepository.deleteById(id); // Xóa khách hàng theo ID
        return "redirect:/customer/list"; // Chuyển hướng về danh sách sau khi xóa
    }
}
