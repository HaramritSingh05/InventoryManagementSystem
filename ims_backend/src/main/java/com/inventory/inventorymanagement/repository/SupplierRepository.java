package com.electrowaveselectronics.inventorymanagement.repository;

import com.electrowaveselectronics.inventorymanagement.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SupplierRepository extends JpaRepository<Supplier, Integer> {



    @Query("SELECT s FROM Supplier s WHERE s.supplierName = :supplierName")
    Supplier findByName(String supplierName);

    @Query("SELECT s FROM Supplier s WHERE s.contactNumber = :contactNumber")
    Supplier findByContactNumber(String contactNumber);

    @Query("SELECT s FROM Supplier s WHERE s.address = :address")
    Supplier findByAddress(String address);


//    @Query("SELECT COUNT(s) > 0 FROM Supplier s WHERE s.address = :address AND s.id != :supplierId")
//    boolean isAddressExistsExceptSupplierId( @Param("address") String address, @Param("supplierId") int supplierId);
}
