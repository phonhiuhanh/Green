package com.poly.greeen.Service;

import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Entity.Order;
import com.poly.greeen.Repository.CustomerRepository;
import com.poly.greeen.Repository.OrderDetailRepository;
import com.poly.greeen.Repository.OrderRepository;
import com.poly.greeen.Security.AuthController;
import com.poly.greeen.Utils.EmailService;
import com.poly.greeen.Utils.SystemStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final SystemStorage systemStorage;
    private final ProductService productService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(Order order) throws Exception {
        var orderDetails = order.getOrderDetails();
        var buyerId = AuthController.getAuthUser().getUniqueId();
        String orderStatus = "Đang chờ xử lý";
        order.setOrderID(orderRepository.getNextOrderId());
        order.setOrderDate(new Date());
        order.setBuyerId(buyerId);
        order.setOrderDetails(null);
        order.setOrderStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);
        for (var orderDetail : orderDetails) {
            orderDetail.setOrder(savedOrder);
            productService.initializeAllProducts();
            orderDetail.setProduct(systemStorage.findProductById(orderDetail.getProduct().getProductID()).orElseThrow());
            orderDetailRepository.save(orderDetail);
        }
        savedOrder.setOrderDetails(orderDetails);
        return savedOrder;
    }

    public Order updateOrder(Integer id, Order orderDetails) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setBuyerId(orderDetails.getBuyerId());
            order.setOrderDate(orderDetails.getOrderDate());
            order.setTotalAmount(orderDetails.getTotalAmount());
            return orderRepository.save(order);
        } else {
            throw new Exception("Không tìm thấy đơn hàng");
        }
    }

    @Transactional
    public Order updateOrderStatus(Integer orderId, String orderStatus) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        } else {
            throw new Exception("Không tìm thấy đơn hàng");
        }
    }

    public void deleteOrder(Integer id) throws Exception {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new Exception("Không tìm thấy đơn hàng");
        }
    }

    public void sendOrderStatusUpdateEmail(Order order, String orderStatus) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByEmailOrPhone(order.getBuyerId(), order.getBuyerId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            emailService.sendOrderStatusUpdateEmail(customer.getEmail(), order, orderStatus);
        } else {
            throw new Exception("Không tìm thấy khách hàng");
        }
    }
}