package com.poly.greeen.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product")
public class Product {
    @Id
    private Integer productID;
    private String name;
    private String description;
    private Double price;
    private Integer giamgia;
    private Double giacu;
    private Integer quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;
}
