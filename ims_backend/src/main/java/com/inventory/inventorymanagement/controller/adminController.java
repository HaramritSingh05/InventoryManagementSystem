package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.entity.Admin;
import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.repository.AdminRepository;
import com.electrowaveselectronics.inventorymanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@RestController
// Adjusted base path
@RequestMapping("/api")
public class adminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody Admin newAdmin) throws Exception{
        return new ResponseEntity<>(adminService.createAdmin(newAdmin), HttpStatus.ACCEPTED);
    }

    @PutMapping("/updateAdminById/{adminId}")
    public ResponseEntity<?> updateAdminById(@PathVariable int adminId , @RequestBody Admin newAdmin) throws Exception {
        return adminService.updateAdminById(adminId, newAdmin);
    }
}
