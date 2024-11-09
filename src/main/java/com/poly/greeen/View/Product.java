package com.poly.greeen.View;

import com.poly.greeen.Repository.CategoryRepository;
import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("product")
public class Product {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    // Hiển thị sản phẩm theo danh mục
    @GetMapping("/list")  // Endpoint này hiển thị sản phẩm theo danh mục
    public String getProductsByCategory(@RequestParam("categoryID") Integer categoryID, Model model) {
        com.poly.greeen.Entity.Category category = categoryRepository.findById(categoryID).orElse(null);
        if (category != null) {
            List<com.poly.greeen.Entity.Product> productList = productRepository.findByCategory(category);
            model.addAttribute("productList", productList);
        }
        return "giaodien/product";
    }
}
