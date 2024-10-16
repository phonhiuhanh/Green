package com.poly.greeen.Entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "Product")
public class Product {
    @Id
    private Integer productID;
    private String name;
    private String description;
    private Double price;
    private Integer giamgia;
    private Double giacu;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;
}
