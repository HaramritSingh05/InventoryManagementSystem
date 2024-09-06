package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.CustomerService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@RestController
// Adjusted base pathz
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    AuthService authService;

    @Autowired
    GodownHeadService godownHeadService;


    @GetMapping("/getAllCustomers")
    @ResponseBody
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                List<Customer> customers = customerService.getAllCustomers();

                    return new ResponseEntity<>(customers, HttpStatus.ACCEPTED);


            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                // Check if the contact number already exists
                if (customerService.isContactNumberExists(customer.getCustomerNo())) {
                    return new ResponseEntity<>("Contact number already exists. Please use a different one.", HttpStatus.BAD_REQUEST);
                }

                Customer newCustomer = customerService.createCustomer(customer);

                if (!Objects.isNull(newCustomer)) {
                    return new ResponseEntity<>("New Customer has been created.", HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>("Something went wrong while creating the customer", HttpStatus.BAD_REQUEST);
                }

            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong while creating the customer", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable int id,  @RequestHeader("Authorization") String authorizationHeader) {

        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                Customer customer = customerService.getCustomerById(id);
                if (customer != null) {
                    return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>("Customer not found with ID: " + id, HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }


        } catch (Exception e) {
            return new ResponseEntity<>("Customer not found with ID: ", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setCustomer")
    public ResponseEntity<?> setCustomer(@RequestBody Customer NewDataCustomer, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                return new ResponseEntity<>(customerService.setCustomer(NewDataCustomer), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            throw  e;
        }
    }

    @PutMapping("/updateCustomerById/{customerId}")
    public ResponseEntity<?> updateCustomerById(@PathVariable int customerId, @RequestBody Customer NewDataCustomer, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                if (customerService.isContactNumberExistsExceptCustomerId(NewDataCustomer.getCustomerNo(), customerId)) {
                    return new ResponseEntity<>("Contact number already exists. Please use a different one.", HttpStatus.BAD_REQUEST);
                } else {
                    Customer newCustomer = customerService.updateCustomer(customerId,NewDataCustomer);

                    return new ResponseEntity<>("Customer updated with customer id: " + customerId,HttpStatus.ACCEPTED);
                }
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.NOT_FOUND);
        }
    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


}
