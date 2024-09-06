package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.dto.PurchaseOrderDTO;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.entity.PurchaseOrder;
import com.electrowaveselectronics.inventorymanagement.entity.Supplier;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import com.electrowaveselectronics.inventorymanagement.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class PurchaseOrderRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private GodownHeadService godownHeadService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/getAllPurchaseOrders")
    public ResponseEntity<?> getAllPurchaseOrders(@RequestHeader("Authorization") String authorizationHeader) throws Exception  {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {

                List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
                return new ResponseEntity<>(purchaseOrders, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPurchaseOrderByPurchaseId/{purchaseId}")
    public ResponseEntity<?> getPurchaseOrderByPurchaseId(@PathVariable int purchaseId , @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {

                Optional<PurchaseOrder> thepurchaseOrder = purchaseOrderService.getPurchaseOrderByPurchaseId(purchaseId);
                return new ResponseEntity<>(thepurchaseOrder, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createPurchaseOrder")
    public ResponseEntity<?> setPurchaseOrder(@RequestBody PurchaseOrderDTO thepurchaseOrderDTO , @RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println(thepurchaseOrderDTO);
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            System.out.println(token);
            String username = authService.findUsernameByToken(token);
            System.out.println(username);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                return new ResponseEntity<>(purchaseOrderService.createPurchaseOrder(thepurchaseOrderDTO), HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/createPurchaseOrder")
//    public ResponseEntity<?> createPurchaseOrder(@RequestBody PurchaseOrder thepurchaseOrderDTO) {
//        try {
//
//            return new ResponseEntity<>(purchaseOrderService.createPurchaseOrder(thepurchaseOrderDTO), HttpStatus.ACCEPTED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/getSupplierByPurchaseId/{purchaseId}")
    public ResponseEntity<?> getSupplierByPurchaseOrderId(@PathVariable int purchaseId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                Optional<Supplier> theSupplier = purchaseOrderService.getSupplierByPurchaseOrderId(purchaseId);
                return new ResponseEntity<>(theSupplier, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/getProductByPurchaseOrderId/{purchaseId}")
//    public ResponseEntity<?> getProductByPurchaseOrderId(@PathVariable int purchaseId) {
//        try {
//            Optional<List<Product>> products = purchaseOrderService.getProductByPurchaseOrderId(purchaseId);
//            return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
//        }
//    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @GetMapping("/getPurchaseOrderCount/{godownId}")
    public ResponseEntity<?> getPurchaseOrderCountByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {

                int parsedGodownId = Integer.parseInt(godownId);
                return purchaseOrderService.getPurchaseOrderCountByGodownId(parsedGodownId);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
        @GetMapping("/getPurchasedProductsCountByGodownId/{godownId}")
    public ResponseEntity<?> getPurchasedProductsCountByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {

                int parsedGodownId = Integer.parseInt(godownId);
                return purchaseOrderService.getPurchasedProductsCountByGodownId(parsedGodownId);
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPurchasedProductsCount")
    public ResponseEntity<?> getPurchasedProductsCount(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {

                return purchaseOrderService.getPurchasedProductsCount();
            }else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
