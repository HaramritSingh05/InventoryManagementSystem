package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Godown;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.repository.GodownRepository;
import com.electrowaveselectronics.inventorymanagement.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    public ProductRepository productRepository;

    @Autowired
    private GodownRepository godownRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> findAll() throws Exception{
        return productRepository.findAll();
    }

    public Product getProduct(int productId) throws Exception{
        try {
            Product product=productRepository.findById(productId).get();
            return product;
        }catch (Exception e){
            throw e;
        }
    }

    public Product save(Product thrproduct) {
        try {
            Product savedProduct=productRepository.save(thrproduct);
            return savedProduct;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Object[]> getDistinctProductsAndTotalQuantity() {
        return productRepository.findDistinctProductsAndTotalQuantity();
    }
    public List<String> getDistinctProductNames() {
        return productRepository.findDistinctProductNames();
    }

    public ResponseEntity<?> listAllProducts() throws Exception{
       try {
           List<Product> productList = productRepository.findAll();
           if(productList.isEmpty()){
               return new ResponseEntity<>("Product List is Empty", HttpStatus.NOT_FOUND);
           }
           return new ResponseEntity<>(productList, HttpStatus.OK);
       }
       catch (Exception e){
           throw e;
       }

    }

    public ResponseEntity<?> productsCountByGodownId(int godownId) throws Exception{
        try {
            validateGodownId(godownId);
            Optional<Godown> optional = godownRepository.findById(godownId);
            if(optional.isEmpty()){
                new ResponseEntity<>("Godown with ID " + godownId + " not found", HttpStatus.NOT_FOUND);
            }

            int totalProductsCount = productRepository.countByGodownId(godownId);
            Integer totalQuantity = productRepository.sumTotalQuantityByGodownId(godownId);

            HashMap<String, Integer> result = new HashMap<>();

            result.put("productsCount", totalProductsCount);
            result.put("totalQuantity", totalQuantity);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }

    }

    public ResponseEntity<?> listLowStockProductsByGodownId(int godownId, double percentage) throws Exception{
        try{
            validateGodownId(godownId);
            Optional<Godown> optional = godownRepository.findById(godownId);
            if(optional.isEmpty()){
                new ResponseEntity<>("Godown with ID " + godownId + " not found", HttpStatus.NOT_FOUND);
            }
            int godownVolume = optional.get().getVolume();
            List<Product> lowStockProducts = productRepository.findProductsUnderCapacityThreshold(godownId, percentage, godownVolume);
            return new ResponseEntity<>(lowStockProducts, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }

    }

    private void validateGodownId(int godownId){
        if(godownId<=0){
            throw new IllegalArgumentException("Invalid Godown ID: " + godownId);
        }

    }
}
