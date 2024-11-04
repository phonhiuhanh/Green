package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Role")
public class Role {
    @Id
    private Integer roleID;
    private String roleName;

//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//    private List<Users> users;
}