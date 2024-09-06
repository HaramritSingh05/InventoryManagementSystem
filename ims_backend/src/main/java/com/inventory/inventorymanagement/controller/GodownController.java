package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.entity.Godown;
import com.electrowaveselectronics.inventorymanagement.entity.Product;
import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.GodownHeadService;
import com.electrowaveselectronics.inventorymanagement.service.GodownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
public class GodownController {
    @Autowired
    private GodownService godownService;

    @Autowired
    private GodownHeadService godownHeadService;

    @Autowired
    private AuthService authService;

    // godown
    @GetMapping("/api/getAllGodown")
    @ResponseBody
    public ResponseEntity<?> getAllGodown(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                return godownService.getAllGodown();
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/getGodown/{godownId}")
    @ResponseBody
    public ResponseEntity<?> getGodownByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                int parsedGodownId = Integer.parseInt(godownId);
                return godownService.getGodownByGodownId(parsedGodownId);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (NumberFormatException e) {
            String errorMessage = "Invalid Godown ID format. Please provide a valid integer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/setGodown")
    public ResponseEntity<?> setGodown(@RequestBody Godown theGodown, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                ResponseEntity<?> responseEntity = godownService.setGodown(theGodown);
                return new ResponseEntity<>("Operation successful: " + responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/createGodown")
    public ResponseEntity<?> createGodown(@RequestBody Godown theGodown, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                ResponseEntity<?> responseEntity = godownService.createGodown(theGodown);
                return new ResponseEntity<>("Operation successful: " + responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/getCapacity/{godownId}")
    @ResponseBody
    public ResponseEntity<?> getCapacityByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
//            System.out.println("token: "+token+" username: "+username);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                int parsedGodownId = Integer.parseInt(godownId);

                return godownService.getCapacityByGodownId(parsedGodownId);

            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (NumberFormatException e) {
            String errorMessage = "Invalid Godown ID format. Please provide a valid integer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/getGodwnCount")
    @ResponseBody
    public long getGodownCount() {
        try {
            return godownService.getGodownCount();
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return -1; // or throw a custom exception
        }
    }

    @PatchMapping("api/updateGodown/{godownId}")
    public ResponseEntity<?> updateGodownByGodownId(@RequestBody Godown theGodown, @PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader) {

        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                int parsedGodownId = Integer.parseInt(godownId);
                return godownService.updateGodownByGodownId(theGodown, parsedGodownId);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        }
        catch (NumberFormatException e) {
            String errorMessage = "Invalid Godown ID format. Please provide a valid integer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // product
    @GetMapping("/api/listProducts/{godownId}")
    @ResponseBody
    public ResponseEntity<?> listProductByGodownId(@PathVariable String godownId, @RequestHeader("Authorization") String authorizationHeader){
        try{
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                int parsedGodownId = Integer.parseInt(godownId);
                return godownService.listProductByGodownId(parsedGodownId);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (NumberFormatException e) {
            String errorMessage = "Invalid Godown ID format. Please provide a valid integer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("api/addProduct")
    public ResponseEntity<?> addProductByGodownId(@RequestBody Product theproduct, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                return godownService.addProductByGodownId(theproduct);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("api/updateProduct")
    public ResponseEntity<?> updateProductByGodownId(@RequestBody Product theproduct, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username) &&
                    ("admin".equals(godownHeadService.getRoleByUsername(username).name())
                            || "godownhead".equals(godownHeadService.getRoleByUsername(username).name()))) {
                return godownService.updateProductByGodownId(theproduct);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/findGodownsByAddress")
    public ResponseEntity<?> findGodownsByAddress(@RequestParam String partialAddress, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                List<Godown> godowns = godownService.findGodownsByAddress(partialAddress);

                return godowns.isEmpty()
                        ? new ResponseEntity<>("No Godowns found for the provided address", HttpStatus.NOT_FOUND)
                        : new ResponseEntity<>(godowns, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied. Please login.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/findGodownsByGodownHead")
    public ResponseEntity<?> findGodownsByGodownHead(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if (!Objects.isNull(username)
                    && "admin".equals(godownHeadService.getRoleByUsername(username).name())
            ) {
                System.out.println("admin exist");
                List<Object[]> godowns = godownService.findGodownsByGodownHead();
                System.out.println(godowns);

                return godowns.isEmpty()
                        ? new ResponseEntity<>("No Godowns found for the provided address", HttpStatus.NOT_FOUND)
                        : new ResponseEntity<>(godowns, HttpStatus.OK);
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