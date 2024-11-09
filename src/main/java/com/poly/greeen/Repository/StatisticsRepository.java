package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Product, Long> {

    // 1. Call GetEntityCounts stored procedure
    @Query(value = "EXEC GetEntityCounts", nativeQuery = true)
    List<Object[]> getEntityCounts();

    // 2. Call GetFinancialStatisticsByYear stored procedure
    @Query(value = "EXEC GetFinancialStatisticsByYear", nativeQuery = true)
    List<Object[]> getFinancialStatisticsByYear();
}
