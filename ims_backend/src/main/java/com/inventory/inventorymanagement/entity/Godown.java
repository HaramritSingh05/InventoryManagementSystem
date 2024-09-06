package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "godown")
@Data
public class Godown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "godown_id") // inventory id
    private int godownId;

    @Column(name = "address")
    private String address;

    @Column(name = "volume")
    private int volume;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "godown_id")
    private List<Product> productList;



    public Godown(){
    }

    public Godown(String address, int volume) {
        this.address = address;
        this.volume = volume;
    }

    // add convenience methods
    public void addProducts(Product tempProduct){
        if(productList==null){
            productList = new ArrayList<>();
        }
        tempProduct.setGodownId(this.godownId);
        productList.add(tempProduct);
    }

    public Product findProductByProductName(String productName){
        for (Product product: productList){
            if(Objects.equals(product.getProductName(), productName)){
                return product;
            }
        }
        return null;
    }


}
