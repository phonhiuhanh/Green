package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Users")
public class Users {
    @Id
    private Integer userID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String city;

    @ManyToOne
    @JoinColumn(name = "roleID")
    private Role role;
}
