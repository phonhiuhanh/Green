package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Import")
public class Import {

    @Id
    private Integer importID;

    private String address;
    private String shipperName;
    private String staffName;
    private String picture;
    private Boolean isDeleted = false;
}
