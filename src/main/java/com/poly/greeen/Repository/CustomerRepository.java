package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmailOrPhone(String email, String phone);
    @Query("SELECT COALESCE(MAX(c.customerID), 0) + 1 FROM Customer c")
    int getNextCustomerId();
}
