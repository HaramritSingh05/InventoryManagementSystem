package com.electrowaveselectronics.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "godown_head_number")
    private String godownheadNo;

    @Column(name = "otp_value")
    private String otpValue;

    @Column(name = "entered_value")
    private String enteredOtp;


    public Otp(String godownheadNo, String otpValue) {
        this.godownheadNo = godownheadNo;
        this.otpValue = otpValue;
    }

}
