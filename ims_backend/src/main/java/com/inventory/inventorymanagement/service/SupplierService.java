
package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Supplier;
import com.electrowaveselectronics.inventorymanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;




    public List<Supplier> getAllSuppliers() throws Exception {
        try {
            List<Supplier> suppliers = supplierRepository.findAll();

            return suppliers;
        } catch (Exception e) {
            throw e;
        }
    }



    public String updateSuppliers(Supplier theSupplier) throws Exception {

        try {
            Optional<Integer> optional = Optional.of(theSupplier.getSupplierId());
            if (optional.isEmpty()) {
                throw new Exception("Supplier id not provided in input, please try again");
            }


            Supplier existingSupplier = supplierRepository.findById(theSupplier.getSupplierId()).orElseThrow(() -> new Exception("Supplier not found for provided id"));

            if (theSupplier.getSupplierName() != null) {
//                Supplier duplicateSupplier = supplierRepository.findByName(theSupplier.getSupplierName());
//                if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
//                    throw new Exception("A supplier with the same name already exists");
//                }
                existingSupplier.setSupplierName(theSupplier.getSupplierName());
            }

            if (theSupplier.getContactNumber() != null) {
//                Supplier duplicateSupplier = supplierRepository.findByContactNumber(theSupplier.getContactNumber());
//                if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
//                    throw new Exception("A supplier with the same contact number already exists");
//                }

                existingSupplier.setContactNumber(theSupplier.getContactNumber());
            }
            if (theSupplier.getAddress() != null) {
                Supplier duplicateSupplier = supplierRepository.findByAddress(theSupplier.getAddress());
                if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                    throw new Exception("A supplier with the same address already exists");
                }
                existingSupplier.setAddress(theSupplier.getAddress());
            }
            Supplier newSupplier = supplierRepository.save(existingSupplier);
            return "Supplier updated with id: " + newSupplier.getSupplierId();
        } catch (Exception e) {
            throw e;
        }
    }

    public String createSupplier(Supplier theSupplier) throws Exception {
        try {
            if (theSupplier.getContactNumber() != null) {
                Supplier duplicateSupplier = supplierRepository.findByContactNumber(theSupplier.getContactNumber());
                if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                    throw new Exception("A supplier with the same contact number already exists");
                }

                if (theSupplier.getContactNumber() != null && !theSupplier.getContactNumber().matches("\\d{10}")) {
                    throw new Exception("Invalid contact number. It should be a 10-digit numeric value.");
                }
            }
            if (theSupplier.getAddress() != null) {
                Supplier duplicateSupplier = supplierRepository.findByAddress(theSupplier.getAddress());
                if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                    throw new Exception("A supplier with the same address already exists");
                }
            }
            Supplier newSupplier = supplierRepository.save(theSupplier);
            return "New supplier added with id: " + newSupplier.getSupplierId();


        } catch (Exception e) {
            throw e;
        }
    }



    public String setSupplier(Supplier theSupplier) {
        try {
            Integer supplierId = theSupplier.getSupplierId();
            if (supplierId == null) {
                throw new Exception("Supplier id not provided in input, please try again");
            }

            Optional<Supplier> optionalSupplier = supplierRepository.findById(theSupplier.getSupplierId());

            if (optionalSupplier.isPresent()) {
                // Updating an existing supplier
                Supplier existingSupplier = optionalSupplier.get();

                if (theSupplier.getSupplierName() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByName(theSupplier.getSupplierName());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same name already exists");
                    }
                    existingSupplier.setSupplierName(theSupplier.getSupplierName());
                }
                if (theSupplier.getContactNumber() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByContactNumber(theSupplier.getContactNumber());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same contact number already exists");
                    }

                    if (theSupplier.getContactNumber() != null && !theSupplier.getContactNumber().matches("\\d{10}")) {
                        throw new Exception("Invalid contact number. It should be a 10-digit numeric value.");
                    }
                    existingSupplier.setContactNumber(theSupplier.getContactNumber());
                }
                if (theSupplier.getAddress() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByAddress(theSupplier.getAddress());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same address already exists");
                    }
                    existingSupplier.setAddress(theSupplier.getAddress());
                }
                Supplier updatedSupplier = supplierRepository.save(existingSupplier);
                return "Supplier updated with id: " + updatedSupplier.getSupplierId();
            } else {
                // Adding a new supplier
                if (theSupplier.getSupplierName() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByName(theSupplier.getSupplierName());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same name already exists");
                    }
                }
                if (theSupplier.getContactNumber() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByContactNumber(theSupplier.getContactNumber());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same contact number already exists");
                    }


                    if (theSupplier.getContactNumber() != null && !theSupplier.getContactNumber().matches("\\d{10}")) {
                        throw new Exception("Invalid contact number. It should be a 10-digit numeric value.");
                    }




                }
                if (theSupplier.getAddress() != null) {
                    Supplier duplicateSupplier = supplierRepository.findByAddress(theSupplier.getAddress());
                    if (duplicateSupplier != null && !(duplicateSupplier.getSupplierId() == (theSupplier.getSupplierId()))) {
                        throw new Exception("A supplier with the same address already exists");
                    }
                }
                Supplier newSupplier = supplierRepository.save(theSupplier);
                return "New supplier added with id: " + newSupplier.getSupplierId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public Optional<Supplier> getSupplierBySupplierId(int supplierId) {
        try {
            return supplierRepository.findById(supplierId);
        } catch (Exception e) {
            throw e;
        }

    }
}
