package com.poly.greeen.Controller;

import com.poly.greeen.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/top10-by-giamgia")
    public ResponseEntity<?> getTop10ProductsByGiamgia(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        try {
            log.info("REST request to get top 10 products by giamgia");
            Pageable topTen = PageRequest.of(0, 10);
            var top10Products = productRepository.findTop10ByOrderByGiamgiaDesc(topTen);
            return ResponseEntity.ok(top10Products);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/top10-by-category")
    public ResponseEntity<?> getTop10ByCategoryID(@RequestParam(value = "categoryID") Integer categoryID,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        try {
            log.info("REST request to get top 10 products by categoryID");
            Pageable topTen = PageRequest.of(0, 10);
            var top10Products = productRepository.findTop10ByCategoryID(categoryID, topTen);
            return ResponseEntity.ok(top10Products);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        try {
            log.info("REST request to get product by id");
            var product = productRepository.findById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
