package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "OrderDetail")
public class OrderDetail {
    @Id
    private Integer orderDetailID;
    private Integer orderID;
    private Integer productID;
    private Integer quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "orderID", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productID", insertable = false, updatable = false)
    private Product product;
}
