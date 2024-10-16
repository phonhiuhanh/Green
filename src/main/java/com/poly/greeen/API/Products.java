package com.poly.greeen.API;

import com.poly.greeen.Entity.Product;
import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@RequestMapping("/sanpham")
@Controller
public class Products {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "/admin/products";
    }
    @PostMapping("/delete")
    public String deleteMotorbike(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa thành công!");
        return "redirect:/Admin/products";
    }

//    @PostMapping("/create")
//    public String createMotorbike(@ModelAttribute MotorbikeMainDto motorbikeMainDto) {
//        Motorbike motorbike = motorbikeMainDto.toEntity();
//        motorbikeRepository.save(motorbike);
//        return "redirect:/Motorbikes";
//    }
}