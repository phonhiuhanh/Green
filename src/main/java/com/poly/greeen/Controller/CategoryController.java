package com.poly.greeen.Controller;

import com.poly.greeen.Repository.CategoryRepository;
import com.poly.greeen.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            log.info("REST request to get all categories");
            var categories = categoryRepository.findAll();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(value = "id") Integer id) {
        try {
            log.info("REST request to get category by id");
            var category = categoryRepository.findById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

}
