package com.poly.greeen.Controller;

import com.poly.greeen.Repository.CategoryRepository;
import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/category")
public class Category {  // Đổi tên class để rõ ràng hơn

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    // Hiển thị tất cả các danh mục
    @GetMapping("/list")  // Đổi endpoint để tránh trùng
    public String getCategories(Model model) {
        List<com.poly.greeen.Entity.Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "giaodien/product";  // Tên file Thymeleaf hiển thị danh mục
    }
}
