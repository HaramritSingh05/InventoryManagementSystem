package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@NoArgsConstructor
public class
Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    @Getter @Setter
    private int supplierId;

    @Getter @Setter
    @Column(name = "supplier_name")
    private String supplierName;

    @Getter @Setter
    @Column(name = "address")
    private String address;

    @Getter @Setter
    @Column(name = "contact_number")
    private String contactNumber;


    public Supplier(int supplierId, String supplierName, String address, String contactNumber) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Supplier(String supplierName, String address, String contactNumber) {
        this.supplierName = supplierName;
        this.address = address;
        this.contactNumber = contactNumber;
    }





}
