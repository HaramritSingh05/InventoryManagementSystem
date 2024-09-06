package com.electrowaveselectronics.inventorymanagement.service;


import com.electrowaveselectronics.inventorymanagement.dto.PurchaseOrderDTO;
import com.electrowaveselectronics.inventorymanagement.dto.PurchaseProductDTO;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.entity.PurchaseOrder;
import com.electrowaveselectronics.inventorymanagement.entity.Supplier;
import com.electrowaveselectronics.inventorymanagement.repository.GodownRepository;
import com.electrowaveselectronics.inventorymanagement.repository.ProductRepository;
import com.electrowaveselectronics.inventorymanagement.repository.PurchaseOrderRepository;
import com.electrowaveselectronics.inventorymanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private GodownRepository godownRepository;

    public List<PurchaseOrder> getAllPurchaseOrders() throws Exception{
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderByPurchaseId(int purchaseId) {
        return purchaseOrderRepository.findById(purchaseId);
    }






    public String createPurchaseOrder(PurchaseOrderDTO thepurchaseOrderDTO) {
        try {
            if ((thepurchaseOrderDTO.getSupplierId() <= 0) || (thepurchaseOrderDTO.getGodownId() < 0)) {
                throw new IllegalArgumentException("Supplier ID and Godown ID must be provided.");
            }

            Optional<Supplier> supplierOptional = supplierRepository.findById(thepurchaseOrderDTO.getSupplierId());
            if (supplierOptional.isEmpty()) {
                throw new IllegalArgumentException("Supplier with the provided ID does not exist.");
            }
            Supplier supplier = supplierOptional.get();
            PurchaseOrder thepurchaseOrder = new PurchaseOrder();
            thepurchaseOrder.setPurchaseDate(new Date());
            thepurchaseOrder.setGodownId(thepurchaseOrderDTO.getGodownId());
            thepurchaseOrder.setSupplierId(thepurchaseOrderDTO.getSupplierId());

            List<PurchaseProductDTO> products = thepurchaseOrderDTO.getProducts();

            for (PurchaseProductDTO purchaseProductDTO : products) {
                if (purchaseProductDTO.getProductName() == null || purchaseProductDTO.getProductVolume() <= 0 || purchaseProductDTO.getCostPrice() <= 0 || purchaseProductDTO.getPurchaseQuantity() <= 0) {
                    throw new IllegalArgumentException("Product name, volume, price, and quantity must be provided.");
                }

                thepurchaseOrder.setPurchaseQuantity(thepurchaseOrder.getPurchaseQuantity() + purchaseProductDTO.getPurchaseQuantity());
                thepurchaseOrder.setTotalCostPrice(thepurchaseOrder.getTotalCostPrice() + purchaseProductDTO.getPurchaseQuantity() * purchaseProductDTO.getCostPrice());
                thepurchaseOrder.addProduct(purchaseProductDTO);

                Product product = productRepository.findProductByGodownIdAndProductName(thepurchaseOrderDTO.getGodownId(), purchaseProductDTO.getProductName());
                if (product != null) {
                    product.setTotalQuantity(product.getTotalQuantity() + purchaseProductDTO.getPurchaseQuantity());
                    productRepository.save(product);
                } else {
                    Product product1 = new Product(thepurchaseOrderDTO.getGodownId(),purchaseProductDTO.getProductName(), purchaseProductDTO.getProductVolume(), purchaseProductDTO.getCostPrice(), purchaseProductDTO.getPurchaseQuantity(), purchaseProductDTO.getProductType(),purchaseProductDTO.getProductCategory());
                    productRepository.save(product1);
                }
            }

            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(thepurchaseOrder);
            return "Purchase order created with id: " + savedPurchaseOrder.getPurchaseId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public String createPurchaseOrder(PurchaseOrder thepurchaseOrder) {
//        try {
//            thepurchaseOrder.setPurchaseDate(new Date());
//            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(thepurchaseOrder);
//            return "Purchase order created with id: " + savedPurchaseOrder.getPurchaseId();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }



    public Optional<Supplier> getSupplierByPurchaseOrderId(int purchaseId) {
        Optional<PurchaseOrder> thePurchaseOrder = purchaseOrderRepository.findById(purchaseId);
        if (thePurchaseOrder.isEmpty()) {
            throw new IllegalArgumentException("Purchase order with the provided ID does not exist.");
        }
        return supplierRepository.findById(thePurchaseOrder.get().getSupplierId());
    }


//    public Optional<List<Product>> getProductByPurchaseOrderId(int purchaseId) {
//        Optional<PurchaseOrder> thePurchaseOrder = purchaseOrderRepository.findById(purchaseId);
//        return Optional.ofNullable(thePurchaseOrder
//    }

    public ResponseEntity<?> getPurchaseOrderCountByGodownId(int godownId) throws Exception {
        try {
            validateGodownId(godownId);
            HashMap<String, Integer> result = new HashMap<>();
            int purchaseOrderCount = purchaseOrderRepository.countByGodownId(godownId);
            result.put("purchaseOrderCount", purchaseOrderCount);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }

    }

    public ResponseEntity<?> getPurchasedProductsCountByGodownId(int godownId) throws Exception {
        try {
            validateGodownId(godownId);
            long result = purchaseOrderRepository.getTotalProductsPurchasedByGodownId(godownId);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }

    }

    public ResponseEntity<?> getPurchasedProductsCount() throws Exception {
        try {
            long result = purchaseOrderRepository.getTotalSumOfPurchaseQuantities();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }

    }

    private void validateGodownId(int godownId){
        if(godownId<=0){
            throw new IllegalArgumentException("Invalid Godown ID: " + godownId);
        }

        if(godownRepository.findById(godownId).isEmpty()){
            throw new IllegalArgumentException("Godown with godownId: "+ godownId + " does not exists.");
        }

    }
}
