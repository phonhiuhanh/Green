package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COALESCE(MAX(o.orderID), 0) + 1 FROM Order o")
    int getNextOrderId();
}
