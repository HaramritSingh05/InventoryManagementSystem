package com.electrowaveselectronics.inventorymanagement.entity;

import com.electrowaveselectronics.inventorymanagement.util.EnumRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "godownHead")
public class GodownHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "godown_head_id")
    private int godownHeadId;

    @Column(name = "godown_head_name")
    private String godownHeadName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private EnumRole role;

    @Column(name = "godown_id")
    private int godownId;

    @Column(name = "address")
    private String address;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "godown_head_number", unique = true)
    private String godownheadNo;

    public GodownHead(String godownHeadName,int godownHeadId, String password, EnumRole role, int godownId, String email, String godownheadNo) {
        this.godownHeadName = godownHeadName;
        this.godownHeadId=godownHeadId;
        this.password = password;
        this.role = role;
        this.godownId=godownId;
        this.email=email;
        this.godownheadNo=godownheadNo;
    }
}


