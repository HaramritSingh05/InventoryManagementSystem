package com.electrowaveselectronics.inventorymanagement.entity;

import com.electrowaveselectronics.inventorymanagement.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_order")
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;


    //   FORIEGN  KEY
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name ="customer_id")
    private Customer customer;


    @Column(name = "order_quantity")
    private int orderQuantity;

    @Column(name = "total_sell_price")

    private int totalSellPrice;

    @Column(name = "tax")
    private int tax;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "expected_date")
    private Date expectedDate;


    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @Embedded
    @Column(name = "product")
    List<ProductDTO> products=new ArrayList<>();

    private int godownId;

    private String godownHeadName;

    private String godownAddress;

    public void addProduct(ProductDTO product){
        products.add(product);
    }

    public void setGodownIds(List<int[]> list) {
    }
}
