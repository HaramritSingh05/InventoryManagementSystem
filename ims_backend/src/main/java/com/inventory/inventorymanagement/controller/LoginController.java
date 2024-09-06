package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.service.AuthService;
import com.electrowaveselectronics.inventorymanagement.service.LoginService;
import com.electrowaveselectronics.inventorymanagement.service.OtpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RestController
@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    AuthService authService;

    @Autowired
    OtpService otpService;

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        try {
//            System.out.println("in login ");
//            System.out.println(loginRequest);
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            return loginService.login(username, password, response);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromAuthorizationHeader(authorizationHeader);
            String username = authService.findUsernameByToken(token);
            if(!Objects.isNull(username)){
                return loginService.logout(response, username);
            }
            return new ResponseEntity<>("User not Logged in", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Somehing went wrong ... "+ e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String godownHeadName = request.get("godownHeadName");
            String email=request.get("email");
            String address=request.get("address");
            String godownheadNo=request.get("godownHeadNo");
            String godownId = request.get("godownId");
            int parsedGodownId = Integer.parseInt(godownId);
            return loginService.register(username, password, godownHeadName,email,godownheadNo, parsedGodownId,address);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String godownheadNo = request.get("godownheadNo");

            // Call a method in your service layer to handle the forgot password logic
            return loginService.sendOtp(godownheadNo);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong... " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/api/verifyotp")
//    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//        System.out.println();
//        try {
//            String godownheadNo = (String) request.get("godownHeadNo");
//            String enteredOtp = request.get("otp");
//
//            // Call a method in your service layer to verify OTP
//            return loginService.verifyOtp(godownheadNo, enteredOtp);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Something went wrong... " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/api/verifyotp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        try {
            String godownheadNo = (String) request.get("godownheadNo");
            String enteredOtp = (String) request.get("otp");

            ResponseEntity<?> response = otpService.verifyOtp(godownheadNo, enteredOtp);
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }
    }



    @PatchMapping("/api/resetpassword")
    public ResponseEntity<?> resetpassword(@RequestBody Map<String, String> request) {
        try {
            String godownheadNo = request.get("godownheadNo");
            String newPassword = request.get("newPassword");


            ResponseEntity<?> response = loginService.resetpassword(godownheadNo, newPassword);
            return response;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }
    }

    @PostMapping("/api/loginWithOtp")
    public ResponseEntity<?> loginWithOtp(@RequestBody Map<String, String> loginRequest) {
        try {
            String godownheadNo = loginRequest.get("godownheadNo");
            String enteredOtp = loginRequest.get("otp");
            return loginService.loginWithOtp(godownheadNo, enteredOtp);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong... " + e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("api/forgot")
//    @ResponseBody
//    public ResponseEntity<?> forgot(@RequestParam("email")String email){
//        System.out.println("EMAIL" +email);
//
//        Random random=new Random(1000);
//        int otp=random.nextInt(99999999);
//        System.out.println("OTP"+otp);
//        return new ResponseEntity<>(otp,HttpStatus.ACCEPTED);
//    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}