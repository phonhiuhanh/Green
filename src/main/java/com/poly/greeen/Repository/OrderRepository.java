package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COALESCE(MAX(o.orderID), 0) + 1 FROM Order o")
    int getNextOrderId();

    @Query("SELECT o FROM Order o WHERE o.orderStatus IN :statuses ORDER BY o.orderDate DESC")
    List<Order> findByOrderStatusIn(@Param("statuses") List<String> statuses);
}
