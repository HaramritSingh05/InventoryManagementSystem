package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Admin;
import com.electrowaveselectronics.inventorymanagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    public AdminRepository adminRepository;

    @Transactional
    public String createAdmin(Admin newAdmin) {
        try {
            Admin admin1 = adminRepository.save(newAdmin);
            return ("New admin has been created with id:" + admin1.getAdminId());
        } catch (Exception e) {
            return ("An error occured");
        }

    }

    @Transactional
    public ResponseEntity<?> updateAdminById(int id, @RequestBody Admin admin) throws Exception {
        try {
            Optional<Admin> existingAdmin = adminRepository.findById(id);
            existingAdmin.get().setAdminUserame(admin.getAdminUserame());
            existingAdmin.get().setAdminPassword(admin.getAdminPassword());
            Admin updatedadmin = adminRepository.save(existingAdmin.get());
            return new ResponseEntity<>("Admin has been updated successfully", HttpStatus.ACCEPTED);


        } catch (Exception e) {
            return new ResponseEntity<>("An error occured while updating the admin", HttpStatus.BAD_REQUEST);

        }
    }


}
