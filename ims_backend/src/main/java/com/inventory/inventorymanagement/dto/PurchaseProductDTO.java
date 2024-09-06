package com.electrowaveselectronics.inventorymanagement.dto;

import lombok.Data;

@Data
public class PurchaseProductDTO {
    private String productName;
    private int purchaseQuantity;
    private float costPrice;
    private int productVolume;
    private float productType;
    private String productCategory;
}
