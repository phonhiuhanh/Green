package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "Customers")
public class Customer {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String city;
}
