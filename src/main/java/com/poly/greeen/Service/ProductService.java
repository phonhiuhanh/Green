package com.poly.greeen.Service;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SystemStorage systemStorage;
    private final ProductImageRepository productImageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> getTop10ProductsByGiamgia(int page, int size) {
        var topTen = PageRequest.of(page, size);
        return productRepository.findTop10ByOrderByGiamgiaDesc(topTen);
    }

    public List<Product> getTop10ByCategoryID(Integer categoryID, int page, int size) {
        var topTen = PageRequest.of(page, size);
        return productRepository.findTop10ByCategoryID(categoryID, topTen);
    }

    public List<Product> getAllProducts() {
        initializeAllProducts();
        return (List<Product>) systemStorage.get("all-products");
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductRequestDTO productRequestDTO, MultipartFile mainImage, MultipartFile[] additionalImages) throws IOException {
        var product = productRequestDTO.toProduct();
        product.setProductID(productRepository.getNextProductId());
        Product savedProduct = productRepository.save(product);
        List<ProductImage> productImages = handleImageUploaded(mainImage, additionalImages, savedProduct);
        productImageRepository.saveAll(productImages);

        product.setImages(productImages);
        updateAllProductsCache();
        return savedProduct;
    }

    public Product updateProduct(ProductRequestDTO productRequestDTO, MultipartFile mainImage, MultipartFile[] additionalImages) throws IOException {
        var product = productRequestDTO.toProduct();
        List<ProductImage> productImages = handleImageUploaded(mainImage, additionalImages, product);
        product.setImages(productImages);
        Product updatedProduct = productRepository.save(product);
        updateAllProductsCache();
        return updatedProduct;
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
        updateAllProductsCache();
    }

    public List<Product> searchProducts(String keyword, Integer categoryID) {
        initializeAllProducts();
        List<Product> allProducts = (List<Product>) systemStorage.get("all-products");

        return allProducts.stream()
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
    }

    private List<ProductImage> handleImageUploaded(MultipartFile mainImage, MultipartFile[] additionalImages, Product savedProduct) throws IOException {
        List<ProductImage> productImages = new ArrayList<>();
        int nextProductImageId = productImageRepository.getNextProductImageId();

        if (!mainImage.isEmpty()) {
            String mainImageURL = saveImage(mainImage);
            ProductImage mainProductImage = new ProductImage();
            mainProductImage.setId(nextProductImageId++);
            mainProductImage.setImageURL(mainImageURL);
            mainProductImage.setIsMain(true);
            mainProductImage.setProduct(savedProduct);
            productImages.add(mainProductImage);
        }

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

    private String saveImage(MultipartFile image) throws IOException {
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return "/uploads/" + filename;
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
}