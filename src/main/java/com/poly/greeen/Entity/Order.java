package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Orders") // Thay đổi tên bảng về 'Order' nếu bạn đã định nghĩa nó như vậy trong SQL
public class Order {
    @Id
    private Integer orderID; // ID tự cung cấp
    private Integer userID; // Khóa ngoại
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate; // Ngày đặt hàng
    private BigDecimal totalAmount; // Tổng số tiền
    private String buyerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}
