package com.poly.greeen.Data;

import com.poly.greeen.Entity.Customer;
import com.poly.greeen.Entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class RegisterRequest {
    private String code;
    private Timestamp createdAt;
    private Customer customer;
    private Users user;
}
