package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "Admin")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId; // provider id


    @Column(name = "admin_username")
    private String adminUserame;

    @Column(name = "admin_password")
    private String adminPassword;

    public Admin(String adminUserame, String adminPassword) {
        this.adminUserame = adminUserame;
        this.adminPassword = adminPassword;
    }

}
