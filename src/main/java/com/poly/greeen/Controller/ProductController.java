package com.poly.greeen.Controller;

import com.poly.greeen.Data.ProductRequestDTO;
import com.poly.greeen.Entity.Product;
import com.poly.greeen.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/top10-by-giamgia")
    public ResponseEntity<?> getTop10ProductsByGiamgia(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        try {
            log.info("Yêu cầu REST để lấy 10 sản phẩm giảm giá hàng đầu");
            var top10Products = productService.getTop10ProductsByGiamgia(page, size);
            return ResponseEntity.ok(top10Products);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping("/top10-by-category")
    public ResponseEntity<?> getTop10ByCategoryID(@RequestParam(value = "categoryID") Integer categoryID,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        try {
            log.info("Yêu cầu REST để lấy 10 sản phẩm hàng đầu theo ID danh mục");
            var top10Products = productService.getTop10ByCategoryID(categoryID, page, size);
            return ResponseEntity.ok(top10Products);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            log.info("Yêu cầu REST để lấy tất cả sản phẩm");
            var products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        try {
            log.info("Yêu cầu REST để lấy sản phẩm theo ID");
            Optional<Product> product = productService.getProductById(id);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/manager")
    public ResponseEntity<?> createProduct(
            @RequestPart("product") ProductRequestDTO productRequestDTO,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("additionalImages") MultipartFile[] additionalImages) {
        try {
            log.info("Yêu cầu REST để tạo sản phẩm");
            Product savedProduct = productService.createProduct(productRequestDTO, mainImage, additionalImages);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/manager")
    public ResponseEntity<?> updateProduct(
            @RequestPart("product") ProductRequestDTO productRequestDTO,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("additionalImages") MultipartFile[] additionalImages) {
        try {
            log.info("Yêu cầu REST để cập nhật sản phẩm");
            Product updatedProduct = productService.updateProduct(productRequestDTO, mainImage, additionalImages);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @DeleteMapping("/manager/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping("/q")
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryID", required = false) Integer categoryID) {
        try {
            log.info("Yêu cầu REST để tìm kiếm sản phẩm");
            List<Product> filteredProducts = productService.searchProducts(keyword, categoryID);
            return ResponseEntity.ok(filteredProducts);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}