package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Category;
import com.poly.greeen.Entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE CAST(p.productID AS string) LIKE %:keyword%")
    List<Product> findByProductIDContaining(@Param("keyword") String keyword);

    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p ORDER BY p.giamgia DESC")
    List<Product> findTop10ByOrderByGiamgiaDesc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.categoryID = :categoryID")
    List<Product> findTop10ByCategoryID(@Param("categoryID") Integer categoryID, Pageable pageable);

}
