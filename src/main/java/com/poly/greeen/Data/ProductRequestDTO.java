package com.poly.greeen.Data;

import com.poly.greeen.Entity.Category;
import com.poly.greeen.Entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductRequestDTO {
    private Integer productID;
    private String name;
    private double giacu;
    private int giamgia;
    private String description;
    private int quantity;
    private int categoryID;
//    private MultipartFile mainImage;
//    private MultipartFile[] additionalImages;

    // Method to convert ProductRequestDTO to Product
    public Product toProduct() {
        Product product = new Product();
        product.setProductID(this.productID);
        product.setName(this.name);
        product.setGiacu(this.giacu);
        product.setGiamgia(this.giamgia);
        product.setPrice(this.giacu * (1 - this.giamgia / 100));
        product.setDescription(this.description);
        product.setQuantity(this.quantity);
        product.setCategory(Category.builder().categoryID(this.categoryID).build());
        // Handle images separately
        return product;
    }

    // Method to convert Product to ProductRequestDTO
    public static ProductRequestDTO fromProduct(Product product) {
        return ProductRequestDTO.builder()
                .name(product.getName())
                .giacu(product.getGiacu())
                .giamgia(product.getGiamgia())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .categoryID(product.getCategory().getCategoryID())
                // Handle images separately
                .build();
    }
}
