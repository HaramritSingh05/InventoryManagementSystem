package com.electrowaveselectronics.inventorymanagement.repository;

import com.electrowaveselectronics.inventorymanagement.entity.GodownHead;
import com.electrowaveselectronics.inventorymanagement.entity.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    // Add custom query methods if needed

    @Modifying
    @Transactional
    @Query("UPDATE Otp otp SET otp.otpValue = :code WHERE otp.godownheadNo = :godownHeadNumber")
    void saveOtp(@Param("godownHeadNumber") String godownHeadNumber, @Param("code") String code);

    @Query("SELECT otp FROM Otp otp WHERE otp.godownheadNo = :godownHeadNumber")
    Otp getOtpByGodownheadNo(@Param("godownHeadNumber") String godownHeadNumber);

    @Query("SELECT otp FROM Otp otp WHERE otp.godownheadNo = :godownHeadNumber")
    Otp findByGodownheadNo(@Param("godownHeadNumber") String godownHeadNumber);

    @Query("SELECT o FROM Otp o WHERE o.godownheadNo = :godownHeadNo AND o.otpValue = :otpValue")
    Otp findByGodownheadNoAndOtpValue(String godownHeadNo, String otpValue);

}
