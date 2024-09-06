package com.electrowaveselectronics.inventorymanagement.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String productName;
    private int orderQuantity;
    private double sellPrice;
    private int taxPercentage=18;
    private double tax;

    public void addTaxAmount(){
        this.tax= this.sellPrice*this.taxPercentage*0.01;
    }

}