package com.poly.greeen.AdminController;

import com.poly.greeen.Entity.OrderDetail;
import com.poly.greeen.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class Order {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/list")
    public String listOrderDetails(Model model) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findAll();
        model.addAttribute("orderDetails", orderDetailList);
        return "admin/order";
    }

    // Phương thức xóa đơn hàng
    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Integer orderID) {
        orderDetailRepository.deleteById(orderID);
        return "redirect:/order";
    }
}
