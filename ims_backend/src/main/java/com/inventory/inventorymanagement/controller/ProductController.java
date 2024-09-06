package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import com.electrowaveselectronics.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GodownHeadService godownHeadService;

    @ResponseBody
    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProducts() {
        try{
            List<Object[]> products = productService.getDistinctProductsAndTotalQuantity();
            if(!products.isEmpty()){
                return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>("Product list is empty" , HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage() , HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/getProduct/{productId}")
    public Product getProduct(@PathVariable int productId) throws Exception {
        Product theProduct=  productService.getProduct(productId);
        if (theProduct==null){
            throw new RuntimeException("Product id not found= " + productId);
        }
        return theProduct;
    }

    @PostMapping("/setProduct")
    public Product addProduct(@RequestBody Product thrproduct){
        thrproduct.setProductId(0);
        return productService.save(thrproduct);
    }

    @GetMapping("/getDistinctProduct")
    public List<String> getDistinctProductNames() {
        return productService.getDistinctProductNames();
    }


    @GetMapping("/listAllProducts")
    public ResponseEntity<?> listAllProducts(@RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            )) {
                return productService.listAllProducts();
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/productsCount/{godownId}")
    public ResponseEntity<?> productsCountByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
                    ) {

                int parsedGodownId = Integer.parseInt(godownId);
                return productService.productsCountByGodownId(parsedGodownId);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/listLowStockProducts")
    public ResponseEntity<?> listLowStockProductsByGodownId(@RequestParam int godownId, @RequestParam double percentage, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {

                return productService.listLowStockProductsByGodownId(godownId, percentage);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }



}
