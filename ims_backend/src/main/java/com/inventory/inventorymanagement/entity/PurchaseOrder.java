package com.electrowaveselectronics.inventorymanagement.entity;

import com.electrowaveselectronics.inventorymanagement.dto.PurchaseProductDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@NoArgsConstructor
@Getter
@Setter
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private int purchaseId; // prod id

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "total_cost_price")
    private float totalCostPrice;


    @Column(name = "purchase_quantity")
    private int purchaseQuantity;

@Column
private int godownId;


    @Column
    private int supplierId;


    public PurchaseOrder(Date purchaseDate, int totalCostPrice, int purchaseQuantity, int supplierId , int godownId) {
        this.purchaseDate = purchaseDate;
        this.totalCostPrice = totalCostPrice;
        this.purchaseQuantity = purchaseQuantity;
        this.supplierId = supplierId;
        this.godownId = godownId;
    }

    @ElementCollection
    @CollectionTable(name = "purchase_products", joinColumns = @JoinColumn(name = "purchase_id"))
    @Embedded
    @Column(name = "product")
    List<PurchaseProductDTO> products=new ArrayList<>();

    public void addProduct(PurchaseProductDTO product){
        products.add(product);
    }
}
