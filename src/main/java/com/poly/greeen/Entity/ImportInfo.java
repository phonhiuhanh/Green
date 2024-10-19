package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "ImportInfo")
public class ImportInfo {

    @Id
    private Integer importInfoID;

    private Integer importID;
    private Integer productID;
    private BigDecimal priceImport;
    private Integer quantityImport;
    private String status;

    @ManyToOne
    @JoinColumn(name = "importID", insertable = false, updatable = false)
    private ImportInfo importInfo;

    @ManyToOne
    @JoinColumn(name = "productID", insertable = false, updatable = false)
    private Product product;
}
