package com.poly.greeen.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;
    private String imageURL;
    private Boolean isMain;
}
