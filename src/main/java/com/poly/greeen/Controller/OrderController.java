package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Order;
import com.poly.greeen.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(required = false) List<String> statuses) {
        try {
            log.info("Yêu cầu REST để lấy tất cả đơn hàng");
            List<Order> orders;
            if (statuses == null || statuses.isEmpty()) {
                orders = orderService.getAllOrders();
            } else {
                orders = orderService.getOrdersByStatuses(statuses);
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        try {
            log.info("Yêu cầu REST để lấy đơn hàng theo ID");
            return orderService.getOrderById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            log.info("Yêu cầu REST để tạo đơn hàng");
            Order savedOrder = orderService.createOrder(order);
            orderService.sendOrderStatusUpdateEmail(savedOrder, savedOrder.getOrderStatus());
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderDetails) {
        try {
            log.info("Yêu cầu REST để c��p nhật đơn hàng");
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String orderStatus) {
        try {
            log.info("Yêu cầu REST để cập nhật trạng thái đơn hàng");
            Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);
            orderService.sendOrderStatusUpdateEmail(updatedOrder, orderStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        try {
            log.info("Yêu cầu REST để xóa đơn hàng");
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().build();
        }
    }
}