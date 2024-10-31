package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Product;
import com.poly.greeen.Repository.ProductRepository;
import com.poly.greeen.Utils.SystemStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final SystemStorage systemStorage;

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

    // Phương thức để kiểm tra và cập nhật SystemStore map
    public void initializeAllProducts() {
        // Kiểm tra nếu SystemStore không có mục với khóa "all-products"
        if (!systemStorage.containsKey("all-products")) {
            // Tạo mục với khóa "all-products" và giá trị là danh sách tất cả sản phẩm từ cơ sở dữ liệu
            List<Product> allProducts = productRepository.findAll();
            systemStorage.put("all-products", allProducts);
        }
    }

    @GetMapping("/q")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryID", required = false) Integer categoryID) {

        // Khởi tạo dữ liệu
        initializeAllProducts();

        // Lấy danh sách sản phẩm từ SystemStore
        List<Product> allProducts = (List<Product>) systemStorage.get("all-products");

//        keyword = keyword != null ? keyword.trim().toLowerCase() : null;

        // Áp dụng bộ lọc dựa trên keyword và categoryID
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> {
                    var name = product.getName().toLowerCase();
                    var category = product.getCategory().getName().toLowerCase();
                    boolean matchesKeyword = (keyword == null || keyword.isEmpty()) ||
                            name.startsWith(keyword.trim().toLowerCase()) ||
                            name.contains(keyword.trim().toLowerCase()) ||
                            category.startsWith(keyword.trim().toLowerCase()) ||
                            category.contains(keyword.trim().toLowerCase());
                    boolean matchesCategory = (categoryID == null) ||
                            product.getCategory().getCategoryID().equals(categoryID);
                    return matchesKeyword && matchesCategory;
                })
                .collect(Collectors.toList());

        // Trả về danh sách sản phẩm đã lọc
        return ResponseEntity.ok(filteredProducts);
    }

}
