package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Godown;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.repository.GodownRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@org.springframework.stereotype.Service
public class GodownService {

    @Autowired
    private GodownRepository godownRepository;

    // Reusable functions
    private void validateGodown(Godown godown) {
        if (StringUtils.isEmpty(godown.getAddress())) {
            throw new IllegalArgumentException("Address must be provided");
        }
        if (godown.getVolume() <= 0) {
            throw new IllegalArgumentException("Volume should have a positive value");
        }
    }

    private void validateGodownId(int godownId){
        if(godownId<=0){
            throw new IllegalArgumentException("Invalid Godown ID: " + godownId);
        }

    }

    public long getGodownCount(){
        return godownRepository.count();
    }

    public ResponseEntity<?> getAllGodown() throws Exception{
        try {
            List<Godown> godowns = godownRepository.findAll();
            return godowns.isEmpty()
                    ? new ResponseEntity<>("No Godowns Found", HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(godowns, HttpStatus.OK);

        }catch (Exception e){
            throw e;
        }
    }
    // admin
    public ResponseEntity<?> setGodown(Godown theGodown) throws Exception {
        validateGodown(theGodown);

        int providedGodownId = theGodown.getGodownId();
        if(providedGodownId>0){
            Optional<Godown> existingGodownOptional = godownRepository.findById(providedGodownId);

            if (existingGodownOptional.isPresent()) {
                // Update existing Godown
                Godown existingGodown = existingGodownOptional.get();

                // Update fields based on the provided Godown
                existingGodown.setAddress(theGodown.getAddress());
                existingGodown.setVolume(theGodown.getVolume());
                existingGodown.setProductList(theGodown.getProductList());

                godownRepository.save(existingGodown);
                return new ResponseEntity<>("Godown updated", HttpStatus.OK);
            }

        }

        if (godownRepository.existsByAddress(theGodown.getAddress())) {
            throw new IllegalArgumentException("Godown with similar address already exists");
        }

        theGodown.setGodownId(0);
        godownRepository.save(theGodown);
        return new ResponseEntity<>("Godown created", HttpStatus.CREATED);
    }

    public ResponseEntity<?> createGodown(Godown theGodown) throws Exception {
        validateGodown(theGodown);

        if (godownRepository.existsByAddress(theGodown.getAddress())) {
            throw new IllegalArgumentException("Godown with similar address already exists");
        }

        theGodown.setGodownId(0);
        godownRepository.save(theGodown);
        return new ResponseEntity<>("Godown created with Godown Id: " + theGodown.getGodownId(), HttpStatus.CREATED);
    }


    public ResponseEntity<?> getGodownByGodownId(int godownId) throws Exception {
        try {
            validateGodownId(godownId);

            Optional<Godown> result = godownRepository.findById(godownId);
            return result.isEmpty()
                    ? new ResponseEntity<>("Godown with ID " + godownId + " not found", HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(result.get(), HttpStatus.OK);

        } catch (Exception e){
            throw e;
        }

    }

    public ResponseEntity<?> updateGodownByGodownId(Godown theGodown, int godownId) throws Exception {
        try {
            validateGodownId(godownId);

            Optional<Godown> optionalGodown = godownRepository.findById(godownId);
            if (optionalGodown.isEmpty()) {
                throw new NoSuchElementException("Godown with ID " + godownId + " not found");
            }

            Godown existingGodown = optionalGodown.get();

            if (!Objects.isNull(theGodown.getAddress())) {
                existingGodown.setAddress(theGodown.getAddress());
            }

            if (theGodown.getVolume() > 0) {
                existingGodown.setVolume(theGodown.getVolume());
            }

            godownRepository.save(existingGodown);
            return new ResponseEntity<>("Godown updated", HttpStatus.OK);


        } catch (Exception e) {
            throw e;
        }
    }


    public ResponseEntity<?> getCapacityByGodownId(int godownId)  throws Exception{
        try {
            validateGodownId(godownId);

            Optional<Godown> optionalGodown = godownRepository.findById(godownId);
            if (optionalGodown.isEmpty()) {
                throw new IllegalArgumentException("Godown with ID " + godownId + " not found");
            }

            Godown tempGodown = optionalGodown.get();
            List<Product> productList = tempGodown.getProductList();
            int capacity = 0;

            for (Product product : productList) {
                capacity += product.getProductVolume() * product.getTotalQuantity();
            }

            int availableCapacity = tempGodown.getVolume() - capacity;

            // Create a HashMap to store the results
            HashMap<String, Integer> capacityMap = new HashMap<>();
            capacityMap.put("totalCapacity", tempGodown.getVolume());
            capacityMap.put("availableCapacity", availableCapacity);

            return ResponseEntity.ok(capacityMap);

        }catch (Exception e){
            throw e;
        }

    }

    // Product
    public ResponseEntity<?> addProductByGodownId(Product theProduct) throws Exception {
        try {
            int godownId = theProduct.getGodownId();
            validateGodownId(godownId);

            Optional<Godown> optionalGodown = godownRepository.findById(godownId);
            if (optionalGodown.isEmpty()) {
                throw new NoSuchElementException("Godown with ID " + godownId + " not found");
            }

            ResponseEntity<?> capacityResponse = getCapacityByGodownId(godownId);
            HashMap<String, Integer> capacityMap = (HashMap<String, Integer>) capacityResponse.getBody();
            int availableCapacity = capacityMap.get("availableCapacity");
            float productVolume = theProduct.getProductVolume() * theProduct.getTotalQuantity();

            if (availableCapacity < productVolume) {
                throw new RuntimeException("Adding this product will exceed the godown's available capacity:" + availableCapacity + " cubic-meters");
            }

            if (theProduct.getProductName() == null) {
                throw new IllegalArgumentException("Product Name must be provided");
            }

            if (theProduct.getProductVolume() <= 0) {
                throw new IllegalArgumentException("Product Volume must be a valid value ");
            }

            if (theProduct.getPrice() <= 0) {
                throw new IllegalArgumentException("Product price must be in valid range");
            }

            if (theProduct.getTotalQuantity() <= 0) {
                throw new IllegalArgumentException("Product totalQuantity must be a positive value");
            }

            if (theProduct.getProductType()  < 0) {
                throw new IllegalArgumentException("Product Type must be a positive value");
            }

            if (theProduct.getProductCategory() == null) {
                throw new IllegalArgumentException("Product Category must be provided");
            }

            Godown tempGodown = optionalGodown.get();
            Product existingProduct = tempGodown.findProductByProductName(theProduct.getProductName());

            if (existingProduct == null) {
                tempGodown.addProducts(theProduct);
                existingProduct = theProduct;
            } else {
                int newTotalQuantity = existingProduct.getTotalQuantity() + theProduct.getTotalQuantity();
                existingProduct.setTotalQuantity(newTotalQuantity);
            }

            godownRepository.save(tempGodown);
            return new ResponseEntity<>("Product added : " + existingProduct.getProductName(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }


    public ResponseEntity<?> updateProductByGodownId(Product theProduct) throws Exception {
        try {
            int godownId = theProduct.getGodownId();

            validateGodownId(godownId);

            Optional<Godown> optionalGodown = godownRepository.findById(godownId);
            if (optionalGodown.isEmpty()) {
                throw new NoSuchElementException("Godown with ID " + godownId + " not found");
            }

            Godown tempGodown = optionalGodown.get();
            Product existingProduct = tempGodown.findProductByProductName(theProduct.getProductName());

            if (existingProduct == null) {
                throw new EntityNotFoundException("Product not found in Godown with ID: " + godownId);
            }

            if (theProduct.getProductVolume() > 0 || theProduct.getTotalQuantity() > 0) {
                ResponseEntity<?> capacityResponse = getCapacityByGodownId(godownId);
                HashMap<String, Integer> capacityMap = (HashMap<String, Integer>) capacityResponse.getBody();
                int availableCapacity = capacityMap.get("availableCapacity");

                float newProductVolume = theProduct.getProductVolume() > 0 ? theProduct.getProductVolume() : existingProduct.getProductVolume();
                int newTotalQuantity = theProduct.getTotalQuantity() > 0 ? theProduct.getTotalQuantity() : existingProduct.getTotalQuantity();

                float netProductVolume = (newProductVolume * newTotalQuantity - existingProduct.getProductVolume() * existingProduct.getTotalQuantity());

                if (netProductVolume > availableCapacity) {
                    throw new RuntimeException("Updating product exceeds available capacity in Godown with capacity " + availableCapacity + " cubic-meters");
                }
            }


            if (theProduct.getProductVolume() > 0) {
                existingProduct.setProductVolume(theProduct.getProductVolume());
            }

            if (theProduct.getPrice() > 0) {
                existingProduct.setPrice(theProduct.getPrice());
            }

            if (theProduct.getTotalQuantity() > 0) {
                existingProduct.setTotalQuantity(theProduct.getTotalQuantity());
            }

            godownRepository.save(tempGodown);
            return new ResponseEntity<>("Product updated: " + existingProduct.getProductName(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<?> listProductByGodownId(int godownId) throws Exception {
        try {
            validateGodownId(godownId);

            Optional<Godown> optionalGodown = godownRepository.findById(godownId);
            if (optionalGodown.isEmpty()) {
                throw new NoSuchElementException("Godown with ID " + godownId + " not found");
            }

            Godown tempGodown = optionalGodown.get();
            List<Product> productList = tempGodown.getProductList();

            return productList.isEmpty()
                    ? new  ResponseEntity<>("Product List is Empty", HttpStatus.NOT_FOUND)
                    : new  ResponseEntity<>(productList, HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Godown> findGodownsByAddress(String partialAddress) {
        return godownRepository.findByAddressLike('%' + partialAddress + '%');
    }



    public List<Object[]> findGodownsByGodownHead() {
        // Additional validation or checks can be added here if needed
        return godownRepository.findGodownWithHeadName();
    }
}
