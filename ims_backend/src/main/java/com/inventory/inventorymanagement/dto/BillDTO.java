package com.electrowaveselectronics.inventorymanagement.dto;

import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class BillDTO {
    //Billing Address
    private String customerName;
    private String customerAddress;
    private String customerContactNo;

    private final String soldBy = "CompanyName";

    // order Details
    private Date orderDate;
    List<ProductDTO> productDTOList;
    private double totalSellPrice;

    @UuidGenerator
    private UUID invoiceNo;
    private Date invoiceDate;

    private String GodownHeadName;
    private String GodownAddress;

}
