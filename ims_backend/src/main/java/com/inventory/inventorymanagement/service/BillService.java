package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.dto.BillDTO;
import com.electrowaveselectronics.inventorymanagement.dto.ProductDTO;
import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.entity.DeliveryOrder;
import com.electrowaveselectronics.inventorymanagement.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class BillService {

    @Autowired
    DeliveryRepository deliveryRepository;


    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong | variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long time_low = (currentTimeMillis & 0x0000_0000_FFFF_FFFFL) << 32;
        final long time_mid = ((currentTimeMillis >> 32) & 0xFFFF) << 16;
        final long version = 1 << 12;
        final long time_hi = ((currentTimeMillis >> 48) & 0x0FFF);
        return time_low | time_mid | version | time_hi;
    }

    public static UUID generateType1UUID() {
        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits);
    }

    public ResponseEntity<?> generateBill(int orderId) throws Exception {
        try {
            DeliveryOrder deliveryOrder = deliveryRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
            if (orderId <= 0) {
                throw new IllegalArgumentException("Invalid order ID");
            }

            Customer customer = deliveryOrder.getCustomer();
            List<ProductDTO> productDTOList = deliveryOrder.getProducts();
            UUID invoiceNo = generateType1UUID();
            Date invoiceDate = new Date();

            BillDTO bill = new BillDTO();
            bill.setCustomerAddress(customer.getCustomerAddress());
            bill.setCustomerName(customer.getCustomerName());
            bill.setCustomerContactNo(customer.getCustomerNo());
            bill.setInvoiceNo(invoiceNo);
            bill.setInvoiceDate(invoiceDate);
            bill.setProductDTOList(productDTOList);
            bill.setOrderDate(deliveryOrder.getOrderDate());
            bill.setTotalSellPrice(deliveryOrder.getTotalSellPrice());
            bill.setGodownHeadName(deliveryOrder.getGodownHeadName());
            bill.setGodownAddress(deliveryOrder.getGodownAddress());

            return new ResponseEntity<>(bill, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }


}
