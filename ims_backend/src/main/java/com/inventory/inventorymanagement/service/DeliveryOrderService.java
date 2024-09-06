
package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.dto.DeliveryOrderDTO;
import com.electrowaveselectronics.inventorymanagement.dto.ProductDTO;
import com.electrowaveselectronics.inventorymanagement.entity.Customer;
import com.electrowaveselectronics.inventorymanagement.entity.DeliveryOrder;
import com.electrowaveselectronics.inventorymanagement.entity.GodownHead;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.repository.CustomerRepository;
import com.electrowaveselectronics.inventorymanagement.repository.DeliveryRepository;
import com.electrowaveselectronics.inventorymanagement.repository.GodownRepository;
import com.electrowaveselectronics.inventorymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeliveryOrderService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    GodownService godownService;

    @Autowired
    GodownRepository godownRepository;
    @Autowired
    GodownHeadService godownHeadService;

    public List<DeliveryOrder> getAllDeliveryOrders() throws Exception {
        try {
            List<DeliveryOrder> deliveryOrders = deliveryRepository.findAll();

            return deliveryOrders;
        } catch (Exception e) {
            throw e;
        }
    }

    public DeliveryOrder  getDeliveryOrderById(int id) throws Exception {

        try {
            Optional<DeliveryOrder> deliveryOrderById = deliveryRepository.findById(id);
            return deliveryOrderById.orElse(null);
        } catch (Exception e) {
            throw e;
        }
    }
    public List<DeliveryOrder> getDeliveryOrderByCustomerId(int customer_id){
        try {

            return deliveryRepository.findByCustomerCustomerId(customer_id);
        } catch (Exception e) {
            throw e;
        }

    }

    public List<DeliveryOrder> getDeliveryOrderByGodownId(int godown_id){
        try {

            return deliveryRepository.findByGodownId(godown_id);
        } catch (Exception e) {
            throw e;
        }

    }


    public DeliveryOrder setOrder(int customerId, DeliveryOrderDTO deliveryOrderDTO) {
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException("Customer not found for ID: " + customerId));
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setOrderDate(new Date());

            int deliveryDays = 7;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, deliveryDays);
            deliveryOrder.setExpectedDate(calendar.getTime());

            int totalQuantity = 0;
            double totalSellprice = 0;
            int godownCount = (int)godownService.getGodownCount();
            for (int godownId = 1; godownId <= godownCount; godownId++) {
                List<ProductDTO> products = deliveryOrderDTO.getProducts();
                boolean orderPlaced = true;
                for (ProductDTO productDTO : products) {
                    // Validate order quantity
                    if (productDTO.getOrderQuantity() <= 0) {
                        throw new IllegalArgumentException("Quantity must not be less than 1");
                    }
                    productDTO.addTaxAmount();
                    totalQuantity += productDTO.getOrderQuantity();
                    totalSellprice += productDTO.getSellPrice() * productDTO.getOrderQuantity();

                    deliveryOrder.addProduct(productDTO);

                    Product product = productRepository.findProductByGodownIdAndProductName(godownId, productDTO.getProductName());
                    int prodQuantityNeeded = productDTO.getOrderQuantity();
                    if (product == null || product.getTotalQuantity() < prodQuantityNeeded) {
                        orderPlaced = false;
                        System.out.println("Order could not be placed in as product's stock is insufficient");
                        break; // Break the inner loop and try next godown

                    }

                }

                if (orderPlaced) {
                    // Reduce product quantities and save delivery order
                    for (ProductDTO productDTO : deliveryOrderDTO.getProducts()) {
                        Product product = productRepository.findProductByGodownIdAndProductName(godownId, productDTO.getProductName());
                        int prodQuantityNeeded = productDTO.getOrderQuantity();
                        int prodQuantityAvailable = product.getTotalQuantity();
                        product.setTotalQuantity(prodQuantityAvailable - prodQuantityNeeded);
                        productRepository.save(product);
                    }
                    deliveryOrder.setTotalSellPrice((int) totalSellprice);
                    deliveryOrder.setOrderQuantity(totalQuantity);
                    deliveryOrder.setCustomer(customer);
                    deliveryOrder.setGodownId(godownId);
                    GodownHead godownHead = godownHeadService.getGodownHeadDetailsByGodownId(godownId);
                    deliveryOrder.setGodownHeadName(godownHead.getGodownHeadName());
                    deliveryOrder.setGodownAddress(godownRepository.findById(godownId).get().getAddress());
                    return deliveryRepository.save(deliveryOrder);
                }
            }
            return null; // Order could not be placed in any godown
        } catch (Exception e) {
            throw e;
        }
    }



//////////////////////////////////////////////

    public ResponseEntity<?> DeliveryOrderCountByGodownId(int godownId) throws Exception{
        try {
            validateGodownId(godownId);
            long saleOrdersCount = deliveryRepository.getTotalSalesCountByGodownID(godownId);
            long totalQuantitiesSold = deliveryRepository.getTotalProductsOrderedByGodownId(godownId);

            HashMap<String, Long> result = new HashMap<>();
            result.put("saleOrdersCount", saleOrdersCount);
            result.put("totalQuantitiesSold", totalQuantitiesSold);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }
    }

    public ResponseEntity<?> getTotalDeliveryProducts() throws Exception{
        try {
            long result = deliveryRepository.getTotalDeliveryProducts();
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }
    }

    public ResponseEntity<?> getTotalSalesOrdersByGodownIDAndDate(int godownID, Date date) throws Exception {
        try {
            validateGodownId(godownID);
            HashMap<String, Long> result = new HashMap<>();
            long salesOnDate = deliveryRepository.getTotalSalesOrdersByGodownIDAndDate(godownID, date);
            long totalQuantitiesSoldOnDate = deliveryRepository.getTotalProductsOrderedByGodownIdAndDate(godownID, date);
            result.put("salesByDate", salesOnDate);
            result.put("totalQuantitiesSoldByDate", totalQuantitiesSoldOnDate);
            return new ResponseEntity<>( result, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }
    }

    public ResponseEntity<?> getTopSellingProducts(int godownId) throws Exception{
        try {
            validateGodownId(godownId);
            List<Object[]> objectList = deliveryRepository.findProductNameAndQuantitySumByGodownIdOrderedBySumDesc(godownId);
            if (objectList.isEmpty()){
                return new ResponseEntity<>("No orders found", HttpStatus.NOT_FOUND);
            }
            return  new ResponseEntity<>(objectList, HttpStatus.OK);

        }
        catch (Exception e){
            throw e;
        }

    }

    private void validateGodownId(int godownId){
        if(godownId<=0){
            throw new IllegalArgumentException("Invalid Godown ID: " + godownId);
        }

        if(godownRepository.findById(godownId).isEmpty()){
            throw new IllegalArgumentException("Godown with godownId: "+ godownId + " does not exists.");
        }
    }

    public List<DeliveryOrder> getOrdersForYear(int godownId, int year) {
        List<DeliveryOrder> allOrders = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>(Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"));

        for (String month : months) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, months.indexOf(month));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            allOrders.addAll(deliveryRepository.getOrdersByDateRange(godownId, startDate, endDate));
        }

        return allOrders;
    }

    public List<DeliveryOrder> getOrdersByDateRange(int godownId, Date startDate, Date endDate) {
        return deliveryRepository.getOrdersByDateRange(godownId, startDate, endDate);
    }

    public List<DeliveryOrder> getProductsForYear(int godownId, int year) {
        return deliveryRepository.getOrdersForYear(godownId,year);
    }
}
