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
import java.util.Optional;

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
    @GetMapping("/search")
    public String getAllProducts(@RequestParam(value = "id", required = false) String id, Model model) {
        List<Product> products;
        if (id != null && !id.isEmpty()) {
            products = productRepository.findByProductIDContaining(id);
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("searchId", id);
        return "/admin/products";
    }
}