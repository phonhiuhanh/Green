package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Import")
public class Import {

    @Id
    private Integer importID;
    private LocalDate date = LocalDate.now();
    private String address;
    private String shipperName;
    private String staffName;
    private String picture;
    private Boolean isDeleted = false;
    private Integer totalAmount;

    @OneToMany(mappedBy = "importDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImportInfo> importInfos;
}
