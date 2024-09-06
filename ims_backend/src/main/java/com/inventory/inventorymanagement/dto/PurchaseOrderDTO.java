package com.electrowaveselectronics.inventorymanagement.dto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseOrderDTO {
    private int purchaseQuantity;

    private float totalCostPrice;

    private int supplierId;

    private int godownId;

    List<PurchaseProductDTO> products; // productName_productVolume

    public PurchaseOrderDTO() {
        this.products = new ArrayList<>();
    }
}
