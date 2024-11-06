package com.poly.greeen.Controller;

import com.poly.greeen.Data.ProductRequestDTO;
import com.poly.greeen.Entity.Product;
import com.poly.greeen.Entity.ProductImage;
import com.poly.greeen.Repository.ProductImageRepository;
import com.poly.greeen.Repository.ProductRepository;
import com.poly.greeen.Utils.SystemStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final SystemStorage systemStorage;
    private final ProductImageRepository productImageRepository;

    @GetMapping("/top10-by-giamgia")
    public ResponseEntity<?> getTop10ProductsByGiamgia(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        try {
            log.info("REST request to get top 10 products by giamgia");
            Pageable topTen = PageRequest.of(page, size);
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
            Pageable topTen = PageRequest.of(page, size);
            var top10Products = productRepository.findTop10ByCategoryID(categoryID, topTen);
            return ResponseEntity.ok(top10Products);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            log.info("REST request to get all products");
            initializeAllProducts();
            var products = (List<Product>) systemStorage.get("all-products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        try {
            log.info("REST request to get product by id");
            Optional<Product> product = productRepository.findById(id);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/manager")
    public ResponseEntity<?> createProduct(
            @RequestPart("product") ProductRequestDTO productRequestDTO,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("additionalImages") MultipartFile[] additionalImages) {
        try {
            log.info("REST request to create product");
            var product = productRequestDTO.toProduct();
            product.setProductID(productRepository.getNextProductId());
            Product savedProduct = productRepository.save(product);
            List<ProductImage> productImages = handleImageUploaded(mainImage, additionalImages, savedProduct);
            productImageRepository.saveAll(productImages);

            product.setImages(productImages);
            updateAllProductsCache();
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    private List<ProductImage> handleImageUploaded(MultipartFile mainImage, MultipartFile[] additionalImages, Product savedProduct) throws IOException {
        List<ProductImage> productImages = new ArrayList<>();
        int nextProductImageId = productImageRepository.getNextProductImageId();

        // Save main image
        if (!mainImage.isEmpty()) {
            String mainImageURL = saveImage(mainImage);
            ProductImage mainProductImage = new ProductImage();
            mainProductImage.setId(nextProductImageId++);
            mainProductImage.setImageURL(mainImageURL);
            mainProductImage.setIsMain(true);
            mainProductImage.setProduct(savedProduct);
            productImages.add(mainProductImage);
        }

        // Save additional images
        for (MultipartFile additionalImage : additionalImages) {
            if (!additionalImage.isEmpty()) {
                String additionalImageURL = saveImage(additionalImage);
                ProductImage additionalProductImage = new ProductImage();
                additionalProductImage.setId(nextProductImageId++);
                additionalProductImage.setImageURL(additionalImageURL);
                additionalProductImage.setIsMain(false);
                additionalProductImage.setProduct(savedProduct);
                productImages.add(additionalProductImage);
            }
        }

        return productImages;
    }

    @Value("${upload.path}")
    private String uploadPath;

    private String saveImage(MultipartFile image) throws IOException {
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return "/uploads/" + filename;
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/manager")
    public ResponseEntity<?> updateProduct(
            @RequestPart("product") ProductRequestDTO productRequestDTO,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart("additionalImages") MultipartFile[] additionalImages) {
        try {
            log.info("REST request to update product");
            var product = productRequestDTO.toProduct();
            List<ProductImage> productImages = handleImageUploaded(mainImage, additionalImages, product);
            product.setImages(productImages);
            Product updatedProduct = productRepository.save(product);
            updateAllProductsCache();
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @DeleteMapping("/manager/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productRepository.deleteById(id);
            updateAllProductsCache();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    private void updateAllProductsCache() {
        List<Product> allProducts = productRepository.findAll();
        systemStorage.put("all-products", allProducts);
    }

    private void initializeAllProducts() {
        if (!systemStorage.containsKey("all-products")) {
            updateAllProductsCache();
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
