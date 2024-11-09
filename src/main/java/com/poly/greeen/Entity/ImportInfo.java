package com.poly.greeen.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "import_info")
public class ImportInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_info_id")
    private Integer importInfoID;

    //    private Integer importID;
//    private Integer productID;
    private BigDecimal priceImport;
    private Integer quantityImport;
    private String status = "Đã nhập";

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "importID")
    private Import importDetail;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    @Override
    public String toString() {
        return "ImportInfo{" +
                "importInfoID=" + importInfoID +
                ", priceImport=" + priceImport +
                ", quantityImport=" + quantityImport +
                ", status='" + status + '\'' +
                ", importDetail=" + (importDetail != null ? importDetail.getImportID() : "null") +
                ", product=" + (product != null ? product.getProductID() : "null") +
                '}';
    }
}
