package com.electrowaveselectronics.inventorymanagement.repository;

import com.electrowaveselectronics.inventorymanagement.entity.GodownHead;
import com.electrowaveselectronics.inventorymanagement.util.EnumRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GodownHeadRepository extends JpaRepository<GodownHead, Integer> {
    GodownHead findByGodownHeadName(String godownHeadName);
    GodownHead findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE GodownHead g SET g.password = :newPassword WHERE g.godownheadNo = :godownheadNo")
    void changePasswordByGodownheadNo(String godownheadNo, String newPassword);

    @Query("SELECT g.role FROM GodownHead g WHERE g.username = :username")
    EnumRole findRoleByUsername(@Param("username")String username);

    @Query("SELECT g FROM GodownHead g WHERE g.godownHeadName = :godownHeadName")
    GodownHead findByName(String godownHeadName);

    @Query("SELECT g FROM GodownHead g WHERE g.godownheadNo = :godownHeadNumber")
    GodownHead findByContactNumber(@Param("godownHeadNumber") String godownHeadNumber);

//    @Modifying
//    @Query("UPDATE GodownHead g SET g.password = :newPassword WHERE g.godownheadNo = :godownHeadNo")
//    void changePassword(@Param("godownHeadNo") String godownHeadNo, @Param("newPassword") String newPassword);



    GodownHead findByGodownId(int godownId);

    boolean existsByEmail(String email);

    boolean existsByGodownheadNo(String godownheadNo);

    @Query("SELECT g FROM GodownHead g WHERE g.email = :email")
    GodownHead findByEmail(String email);

}
