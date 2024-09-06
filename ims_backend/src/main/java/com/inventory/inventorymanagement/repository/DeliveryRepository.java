package com.electrowaveselectronics.inventorymanagement.repository;

import com.electrowaveselectronics.inventorymanagement.dto.ProductDTO;
import com.electrowaveselectronics.inventorymanagement.entity.DeliveryOrder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository

public  interface DeliveryRepository extends JpaRepository<DeliveryOrder,Integer> {
    @Query("SELECT d FROM DeliveryOrder d WHERE d.customer.customerId = :customerId")
    List<DeliveryOrder> findByCustomerCustomerId(@Param("customerId")int customerId);

   List<DeliveryOrder> findByGodownId(@Param("godownId")int godownId);

    @Query("SELECT COALESCE(COUNT(do), 0) FROM DeliveryOrder do WHERE do.godownId = :godownId")
    long getTotalSalesCountByGodownID(@Param("godownId") int godownId);
    @Query("SELECT COALESCE(SUM(do.orderQuantity), 0) FROM DeliveryOrder do WHERE do.godownId = :godownId")
    long getTotalProductsOrderedByGodownId(@Param("godownId") int godownId);

    @Query("SELECT COALESCE(SUM(do.orderQuantity), 0) FROM DeliveryOrder do")
    long getTotalDeliveryProducts();

    @Query("SELECT COALESCE(COUNT(do), 0) FROM DeliveryOrder do WHERE do.godownId = :godownId AND DATE(do.orderDate) = :date")
    long getTotalSalesOrdersByGodownIDAndDate(@Param("godownId") int godownId, @Param("date") Date date);
    @Query("SELECT COALESCE(SUM(do.orderQuantity), 0) FROM DeliveryOrder do WHERE do.godownId = :godownId AND DATE(do.orderDate) = :date")
    long getTotalProductsOrderedByGodownIdAndDate(@Param("godownId") int godownId, @Param("date") Date date);

    @Query("SELECT d.products FROM DeliveryOrder d WHERE d.godownId = :godownId")
    List<ProductDTO> findProductsByGodownId(@Param("godownId") int godownId);

    @Query("SELECT p.productName, SUM(p.orderQuantity) " +
            "FROM DeliveryOrder d JOIN d.products p " +
            "WHERE d.godownId = :godownId " +
            "GROUP BY p.productName " +
            "ORDER BY SUM(p.orderQuantity) DESC")
    List<Object[]> findProductNameAndQuantitySumByGodownIdOrderedBySumDesc(@Param("godownId") int godownId);


    @Query("SELECT do FROM DeliveryOrder do WHERE do.godownId = :godownId AND do.orderDate BETWEEN :startDate AND :endDate")
    List<DeliveryOrder> getOrdersByDateRange(@Param("godownId") int godownId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

   @Query("SELECT d FROM DeliveryOrder d WHERE d.godownId = :godownId AND YEAR(d.orderDate) = :year")
   List<DeliveryOrder> getOrdersForYear(int godownId, int year);

}
