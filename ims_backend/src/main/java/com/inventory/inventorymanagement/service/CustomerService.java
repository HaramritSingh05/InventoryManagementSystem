package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CustomerService {

    @Autowired
    public CustomerRepository customerRepository;



    public List<Customer> getAllCustomers() throws Exception {
        try {
            List<Customer> customers = customerRepository.findAll();
            return customers;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Customer createCustomer(Customer customer) throws Exception {
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw e;
        }
    }

    public Customer getCustomerById(int customerId) throws Exception {
        try {
            Customer customer = customerRepository.findById(customerId).get();
            return customer; // Make sure to return the customer object
        } catch (Exception e) {
            throw e;
        }
    }



    public Customer updateCustomer(int customerId, Customer newDataCustomer) throws Exception {
        try {
            Customer existingCustomer = getCustomerById(customerId);
            existingCustomer.setCustomerName(newDataCustomer.getCustomerName());
            existingCustomer.setCustomerAddress(newDataCustomer.getCustomerAddress());
            existingCustomer.setCustomerNo(newDataCustomer.getCustomerNo());

            return customerRepository.save(existingCustomer);

        }catch(Exception e){
            throw e;

        }

    }

    public boolean isIdExists(int id) {
        return customerRepository.existsById(id);
    }

    public String setCustomer(Customer theCustomer) {
        try {
            Integer customerId = theCustomer.getCustomerId();
            if (customerId == null) {
                throw new Exception("Customer id not provided in input, please try again");
            }

            Optional<Customer> optionalCustomer = customerRepository.findById(theCustomer.getCustomerId());

            if (optionalCustomer.isPresent()) {
                // Updating an existing customer
                Customer existingCustomer = optionalCustomer.get();

                if (theCustomer.getCustomerName() != null) {
                    Customer duplicateCustomer = customerRepository.findByName(theCustomer.getCustomerName());
                    if (duplicateCustomer != null && !(duplicateCustomer.getCustomerId() == (theCustomer.getCustomerId()))) {
                        throw new Exception("A customer with the same name already exists");
                    }
                    existingCustomer.setCustomerName(theCustomer.getCustomerName());
                }
                if (theCustomer.getCustomerNo() != null) {
                    Customer duplicateCustomer = customerRepository.findByContactNumber(theCustomer.getCustomerNo());
                    if (duplicateCustomer != null && !(duplicateCustomer.getCustomerId() == (theCustomer.getCustomerId()))) {
                        throw new Exception("A customer with the same contact number already exists");
                    }

                    if (!theCustomer.getCustomerNo().matches("\\d{10}")) {
                        throw new Exception("Invalid contact number. It should be a 10-digit numeric value.");
                    }
                    existingCustomer.setCustomerNo(theCustomer.getCustomerNo());
                }
                if (theCustomer.getCustomerAddress() != null) {
                    Customer duplicateCustomer = customerRepository.findByAddress(theCustomer.getCustomerAddress());
                    if (duplicateCustomer != null && !(duplicateCustomer.getCustomerId() == (theCustomer.getCustomerId()))) {
                        throw new Exception("A customer with the same address already exists");
                    }
                    existingCustomer.setCustomerAddress(theCustomer.getCustomerAddress());
                }
                Customer updatedCustomer = customerRepository.save(existingCustomer);
                return "Customer updated with id: " + updatedCustomer.getCustomerId();
            } else {
                // Adding a new customer
                if (theCustomer.getCustomerName() != null) {
                    Customer duplicateCustomer = customerRepository.findByName(theCustomer.getCustomerName());
                    if (duplicateCustomer != null) {
                        throw new Exception("A customer with the same name already exists");
                    }
                }
                if (theCustomer.getCustomerNo() != null) {
                    Customer duplicateCustomer = customerRepository.findByContactNumber(theCustomer.getCustomerNo());
                    if (duplicateCustomer != null) {
                        throw new Exception("A customer with the same contact number already exists");
                    }

                    if (!theCustomer.getCustomerNo().matches("\\d{10}")) {
                        throw new Exception("Invalid contact number. It should be a 10-digit numeric value.");
                    }
                }
                if (theCustomer.getCustomerAddress() != null) {
                    Customer duplicateCustomer = customerRepository.findByAddress(theCustomer.getCustomerAddress());
                    if (duplicateCustomer != null) {
                        throw new Exception("A customer with the same address already exists");
                    }
                }
                Customer newCustomer = customerRepository.save(theCustomer);
                return "New customer added with id: " + newCustomer.getCustomerId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isContactNumberExistsExceptCustomerId(String customerNo, int customerId) {
        return customerRepository.isContactNumberExistsExceptCustomerId(customerNo,customerId);
    }

    public boolean isContactNumberExists(String customerNo) {
        return customerRepository.existsByCustomerNo(customerNo);
    }
}