package com.poly.greeen.Repository;

import com.poly.greeen.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query("SELECT COALESCE(MAX(p.id), 0) + 1 FROM ProductImage p")
    int getNextProductImageId();
}
