
package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.entity.Supplier;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import com.electrowaveselectronics.inventorymanagement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class SupplierRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private GodownHeadService godownHeadService;
    @Autowired
    private SupplierService supplierService;

    // injecting supplier using constructor injection

    // expose "/suppliers" and return a list of suppliers

    @GetMapping("/getAllSuppliers")
    public ResponseEntity<?> getAllSuppliers(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                List<Supplier> suppliers = supplierService.getAllSuppliers();
                return new ResponseEntity<>(suppliers, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }
    }

    // add mapping for GET /suppliers/{supplierId}
    @GetMapping("/getSupplierBySupplierId/{supplierId}")
    public ResponseEntity<?> getSupplierBySupplierId(@PathVariable int supplierId,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                Optional<Supplier> theSupplier = supplierService.getSupplierBySupplierId(supplierId);
                return new ResponseEntity<>(theSupplier, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }
    }

    // add mapping for POST /suppliers - add new supplier

    @PostMapping("/setSupplier")
    public ResponseEntity<?> setSupplier(@RequestBody Supplier theSupplier,@RequestHeader("Authorization") String authorizationHeader) {

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

//        theSupplier.setSupplierId(0);

        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                return new ResponseEntity<>(supplierService.setSupplier(theSupplier), HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.NOT_FOUND);
        }


    }

    // add mapping for PUT /suppliers - update existing supplier

    @PutMapping("/updateSuppliers")
    public ResponseEntity<?> updateSuppliers(@RequestBody Supplier theSupplier,@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                return new ResponseEntity<>(supplierService.updateSuppliers(theSupplier), HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/createSupplier")
    public ResponseEntity<?> createSupplier(@RequestBody Supplier theSupplier , @RequestHeader("Authorization") String authorizationHeader) {

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

//        theSupplier.setSupplierId(0);

        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                return new ResponseEntity<>(supplierService.createSupplier(theSupplier), HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }


    }



    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }



}
