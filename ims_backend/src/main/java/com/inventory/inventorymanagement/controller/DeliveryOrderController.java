package com.electrowaveselectronics.inventorymanagement.controller;
import com.electrowaveselectronics.inventorymanagement.dto.DeliveryOrderDTO;
import com.electrowaveselectronics.inventorymanagement.entity.DeliveryOrder;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.DeliveryOrderService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class DeliveryOrderController {
    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GodownHeadService godownHeadService;

    //FOR ADMIN USE

    @GetMapping("/getDeliveryOrders")
    @ResponseBody
    public ResponseEntity<?> getAllDeliveryOrders(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                List<DeliveryOrder> deliveryOrders = deliveryOrderService.getAllDeliveryOrders();
                return new ResponseEntity<>(deliveryOrders, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getDeliveryOrdersById/{id}")
    @ResponseBody
    public ResponseEntity<?> getDeliveryOrderById(@PathVariable int id,@RequestHeader("Authorization") String authorizationHeader) {

        try {
                String token = extractTokenFromAuthorizationHeader(authorizationHeader);
                String username = authService.findUsernameByToken(token);

                if (!Objects.isNull(username) &&
                        ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                                || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
                ) {
                    DeliveryOrder deliveryOrder = deliveryOrderService.getDeliveryOrderById(id);

                    if (deliveryOrder != null) {
                        return new ResponseEntity<>(deliveryOrder, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Delivery Order not found", HttpStatus.NOT_FOUND);
                    }
                } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // FOR CUSTOMER USE
    @PostMapping("/placeOrder/{customerId}")
    public ResponseEntity<?> setOrder(@PathVariable int customerId, @RequestBody DeliveryOrderDTO deliveryOrderDTO,@RequestHeader("Authorization") String authorizationHeader) {
        try {

            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            System.out.println(token+ username);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            )
            ) {
                DeliveryOrder newDeliveryOrder = deliveryOrderService.setOrder(customerId, deliveryOrderDTO);
                if (!Objects.isNull(newDeliveryOrder)) {
                    return new ResponseEntity<>("ORDER HAS BEEN PLACED SUCESSFULLY", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Order could not be placed in as product's stock is insufficient", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Access denied. Please login as Admin.", HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/placedOrderDetails/{customerId}")
    public ResponseEntity<?> orderDetails(@PathVariable int customerId,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name())
                            )
            ) {

                List<DeliveryOrder> newDeliveryOrder = deliveryOrderService.getDeliveryOrderByCustomerId(customerId);

                if (!newDeliveryOrder.isEmpty()) { // Corrected from !newDeliveryOrder.isEmpty()
                    return new ResponseEntity<>(newDeliveryOrder, HttpStatus.OK); // Return the actual object inside Optional
                } else {
                    return new ResponseEntity<>("YOU DIDN'T PLACED ANY ORDER", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getDeliveryOrdersByGodownId/{godownId}")
    public ResponseEntity<?> deliveryOrderDetails(@PathVariable int godownId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                List<DeliveryOrder> newDeliveryOrder = deliveryOrderService.getDeliveryOrderByGodownId(godownId);

                if (!newDeliveryOrder.isEmpty()) { // Corrected from !newDeliveryOrder.isEmpty()
                    return new ResponseEntity<>(newDeliveryOrder, HttpStatus.OK); // Return the actual object inside Optional
                } else {
                    return new ResponseEntity<>("YOU DIDN'T HAVE ANY DELIVERY ORDER", HttpStatus.BAD_REQUEST);
                }

            }
            else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getTotalDeliveryProducts")
    public ResponseEntity<?> getTotalDeliveryProducts(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                return deliveryOrderService.getTotalDeliveryProducts();
            }
            else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }
    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @GetMapping("/getTotalSalesCount/{godownId}")
    public ResponseEntity<?> deliveryOrderCountByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                int parsedGodownId = Integer.parseInt(godownId);
                return deliveryOrderService.DeliveryOrderCountByGodownId(parsedGodownId);

            }
            else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getSalesByDate")
    public ResponseEntity<?> getSalesOnDateByGodownId(@RequestParam("godownId") int godownId, @RequestParam("date") String date, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                Date parsedDate;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                parsedDate = dateFormat.parse(date);
                return deliveryOrderService.getTotalSalesOrdersByGodownIDAndDate(godownId, parsedDate);
            }
            else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getSalesByMonth/{godownId}")
    public ResponseEntity<?> getSalesByMonth(@PathVariable("godownId") int godownId,
                                            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                List<DeliveryOrder> orders = deliveryOrderService.getOrdersForYear(godownId, Calendar.getInstance().get(Calendar.YEAR));

                // Group orders by Month
                List<Map<String, Object>> salesByMonth = groupOrdersByMonth(orders);

                return new ResponseEntity<>(salesByMonth, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }
    }

    private List<Map<String, Object>> groupOrdersByMonth(List<DeliveryOrder> orders) {
        List<Map<String, Object>> salesByMonthList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();


        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
            String abbreviatedMonth = dateFormat.format(calendar.getTime());

            Map<String, Object> MonthData = new LinkedHashMap<>();
            MonthData.put("Month",abbreviatedMonth);
            MonthData.put("salesCount", 0L);
            // 0L intializes the sales count to 0
            salesByMonthList.add(MonthData);
        }

        for (DeliveryOrder order : orders) {
            calendar.setTime(order.getOrderDate());
            int MonthNumber = calendar.get(Calendar.MONTH);
            Map<String, Object> MonthData = salesByMonthList.get(MonthNumber);
            MonthData.put("salesCount", (Long) MonthData.get("salesCount") + 1);
        }

        return salesByMonthList;
    }



    @GetMapping("/getOrderQuantityByMonth/{godownId}")
    public ResponseEntity<?> getOrderQuantityByMonth(@PathVariable("godownId") int godownId,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                List<DeliveryOrder> orders = deliveryOrderService.getProductsForYear(godownId, Calendar.getInstance().get(Calendar.YEAR));

                // Group orders by week
                List<Map<String, Object>> orderQuantityByMonth = groupProductsByMonth(orders);

                return new ResponseEntity<>(orderQuantityByMonth, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }
    }

    private List<Map<String, Object>> groupProductsByMonth(List<DeliveryOrder> orders) {
        List<Map<String, Object>> orderQuantityByMonthList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Initialize orderQuantityByMonthList with empty order quantity for each month
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
            String abbreviatedMonth = dateFormat.format(calendar.getTime());

            Map<String, Object> monthData = new LinkedHashMap<>();
            monthData.put("Month", abbreviatedMonth); // Use abbreviated month name as the key
            monthData.put("orderQuantity", 0); // Initialize order quantity to 0
            orderQuantityByMonthList.add(monthData);
        }

        // Count order quantity for each month
        for (DeliveryOrder order : orders) {
            calendar.setTime(order.getOrderDate());
            int monthNumber = calendar.get(Calendar.MONTH);
            Map<String, Object> monthData = orderQuantityByMonthList.get(monthNumber);
            int orderQuantity = (int) monthData.get("orderQuantity");
            orderQuantity += order.getOrderQuantity(); // Add order quantity to existing quantity
            monthData.put("orderQuantity", orderQuantity);
        }

        return orderQuantityByMonthList;
    }

    @GetMapping("/getTopSellingProducts/{godownId}")
    public ResponseEntity<?> getTopSellingProductsByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);

            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))
            ) {
                int parsedGodownId = Integer.parseInt(godownId);
                return deliveryOrderService.getTopSellingProducts(parsedGodownId);
            }
            else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("FAILED TO FETCH ORDER DETAILS", HttpStatus.BAD_REQUEST);
        }

    }
}