package com.poly.greeen.Repository;

import com.poly.greeen.Entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
//    @Query("SELECT COALESCE(MAX(o.orderDetailID), 0) + 1 FROM OrderDetail o")
//    int getNextOrderDetailId();
}
