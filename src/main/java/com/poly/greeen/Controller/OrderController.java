package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Order;
import com.poly.greeen.Repository.OrderDetailRepository;
import com.poly.greeen.Repository.OrderRepository;
import com.poly.greeen.Security.AuthController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    // Lấy danh sách tất cả các đơn hàng
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    // Lấy thông tin đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới đơn hàng
    @PostMapping
    @Transactional
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        var orderDetails = order.getOrderDetails();
        var buyerId = AuthController.getAuthUser().getUniqueId();
        order.setOrderID(orderRepository.getNextOrderId());
        order.setOrderDate(new Date());
        order.setBuyerId(buyerId);
        order.setOrderDetails(null);
        Order savedOrder = orderRepository.save(order);
        for (var orderDetail : orderDetails) {
            orderDetail.setOrder(savedOrder);
            orderDetailRepository.save(orderDetail);
        }
        savedOrder.setOrderDetails(orderDetails);
        return ResponseEntity.ok(savedOrder);
    }

    // Cập nhật thông tin đơn hàng
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            // Cập nhật thông tin đơn hàng
            order.setBuyerId(orderDetails.getBuyerId());
            order.setOrderDate(orderDetails.getOrderDate());
            order.setTotalAmount(orderDetails.getTotalAmount());
            // Lưu lại đơn hàng đã cập nhật
            Order updatedOrder = orderRepository.save(order);
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa đơn hàng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}