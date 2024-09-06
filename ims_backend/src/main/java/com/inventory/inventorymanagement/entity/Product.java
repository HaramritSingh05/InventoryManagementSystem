package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Column(name = "godown_id")
    private int godownId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId; // prod id

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_volume")
    private float productVolume;

    @Column(name = "price")
    private float price;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "product_type")
    private float productType;

    @Column(name = "product_category")
    private String productCategory;

    public Product(String productName, int productVolume, float price, int totalQuantity, int godownId) {
        this.productName = productName;
        this.productVolume = productVolume;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.godownId = godownId;
    }

    public Product(int godownId, String productName, int productVolume, float price, int totalQuantity, float productType, String productCategory) {
        this.godownId = godownId;
        this.productName = productName;
        this.productVolume = productVolume;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.productType = productType;
        this.productCategory = productCategory;
    }

}
