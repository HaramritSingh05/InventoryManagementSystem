package com.electrowaveselectronics.inventorymanagement.dto;

import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeliveryOrderDTO {
    private int orderQuantity;

    private double totalSellPrice;

    List<ProductDTO> products; // productName_productVolume

    public DeliveryOrderDTO() {
        this.products = new ArrayList<>();
    }
}