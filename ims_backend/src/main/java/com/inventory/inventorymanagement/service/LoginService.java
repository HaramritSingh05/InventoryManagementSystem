package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.GodownHead;
import com.electrowaveselectronics.inventorymanagement.repository.GodownHeadRepository;
import com.electrowaveselectronics.inventorymanagement.repository.OtpRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class LoginService {
    @Autowired
    GodownHeadRepository godownHeadRepository;

    @Autowired
    GodownHeadService  godownHeadService;

    @Autowired
    AuthService authService;

    @Autowired
    OtpService otpService;

    @Autowired
    OtpRepository otpRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Map<String, String> otpMap = new HashMap<>();

    public ResponseEntity<?> login(String username, String password, HttpServletResponse response) throws Exception {
        try {
            if (username == null || password == null) {
                Map<String, String> result = new HashMap<>();
                result.put("error", "Both username and password are required.");
                return ResponseEntity.badRequest().body(result);
            }

            if (!isValidUsername(username)) {
                Map<String, String> result = new HashMap<>();
                result.put("message", "Invalid username format. " +
                        "Should not start with any special char or numeric value, " +
                        "Should not contain whitespace");
                return ResponseEntity.badRequest().body(result);
            }

            GodownHead godownHead = godownHeadRepository.findByUsername(username);

            if(godownHead == null){
                Map<String, String> result = new HashMap<>();
                result.put("message", "Username does not exist");
                return ResponseEntity.badRequest().body(result);

            }

            if (validatePassword(godownHead, password)) {
                // Successful login, set a cookie
                Cookie  cookie = generateUserCookie(username);
                response.addCookie(cookie);
                response.setHeader("Authorization", "Bearer " + cookie.getValue());

                // Create and store Auth object
                authService.createAuthInfo(username, cookie.getValue());
                Map<String, String> result = new HashMap<>();
                result.put("message", "Successfully login.");
                result.put("cookie", cookie.getValue());
                result.put("username", godownHead.getUsername());
                result.put("godownId", String.valueOf(godownHead.getGodownId()));
                result.put("godownHeadId", String.valueOf(godownHead.getGodownHeadId()));
                result.put("role", godownHead.getRole().name());

                return ResponseEntity.accepted().body(result);
            } else {
                Map<String, String> result = new HashMap<>();
                result.put("message", "Invalid password");
                return ResponseEntity.badRequest().body(result);
            }
        }
        catch (Exception e){
            throw e;
        }
    }

    private boolean isValidUsername(String username) {
        // Check for whitespace, and username not starting with a numeric value or special character
        return !Pattern.compile("\\s").matcher(username).find() &&
                !Character.isDigit(username.charAt(0)) &&
                !Pattern.compile("[^a-zA-Z0-9]").matcher(username).find();
    }

    private boolean validatePassword(GodownHead godownHead, String password) {
        // Add your password validation logic here
        return passwordEncoder.matches(password, godownHead.getPassword());
    }

    private Cookie generateUserCookie(String username) {
        String secretKey = "secret";
        String data = username + System.currentTimeMillis() + secretKey;
        String token = generateToken(data);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
        return cookie;
    }

    private String generateToken(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(data.getBytes());

            String encodedHash = Base64.getUrlEncoder().encodeToString(hash);

            return encodedHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> logout(HttpServletResponse response, String username) throws Exception {
        try{
            if (!username.isEmpty()) {
                Cookie cookie = generateUserCookie(username);
                cookie.setMaxAge(0);
                authService.createAuthInfo(username, cookie.toString());
                HttpHeaders headers = new HttpHeaders();
                headers.setCacheControl(CacheControl.noStore());

                return ResponseEntity.ok("Logout successful for user: " + username);
            } else {
                return ResponseEntity.badRequest().body("User not logged in.");
            }
        }
        catch (Exception e){
            throw e;
        }
    }

    public ResponseEntity<?> register(String username, String password, String GodownHeadName, String email, String godownheadNo, int GodownId,String address) {
        if (username == null || password == null || GodownHeadName == null || email==null || godownheadNo==null || GodownId<=0) {
            return ResponseEntity.badRequest().body("Username, password, and GodownHeadName cannot be null or empty");
        }

        if (!isValidUsername(username)) {
            return new ResponseEntity<>("Invalid username format. " +
                    "Should not start with any special char or numeric value, " +
                    "Should not contain whitespace", HttpStatus.BAD_REQUEST);
        }

        if (godownHeadRepository.findByUsername(username)!=null) {
            return new  ResponseEntity<>("Username already taken",HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(password);
        GodownHead newGodownHead = godownHeadService.registerGodownHead(username, hashedPassword, GodownHeadName,email,godownheadNo, GodownId, address);

        Cookie cookie = generateUserCookie(username);

        authService.createAuthInfo(newGodownHead.getUsername(), cookie.getValue());

        return ResponseEntity.ok("Registration successful");
    }


    public ResponseEntity<?> registerAdmin(String username, String password, String GodownHeadName, String email, String godownheadNo) {
        if (username == null || password == null || GodownHeadName == null || email==null || godownheadNo==null) {
            return ResponseEntity.badRequest().body("Username, password, and GodownHeadName cannot be null or empty");
        }

        if (!isValidUsername(username)) {
            return ResponseEntity.badRequest().body("Invalid username format. " +
                    "Should not start with any special char or numeric value, " +
                    "Should not contain whitespace");
        }

        if (godownHeadRepository.findByUsername(username)!=null) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        String hashedPassword = passwordEncoder.encode(password);
        GodownHead newGodownHead = godownHeadService.registerAdmin(username, hashedPassword, GodownHeadName, email,godownheadNo);

        Cookie cookie = generateUserCookie(username);

        authService.createAuthInfo(newGodownHead.getUsername(), cookie.getValue());

        return ResponseEntity.ok("Admin registeration successful");
    }

    private String generateOtp() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }


    public ResponseEntity<?> sendOtp(String godownheadNo) {

        try {
            System.out.println(godownheadNo);
            Map<String, String> otpMap = new HashMap<>();

            GodownHead godownHead = godownHeadRepository.findByContactNumber(godownheadNo);
            System.out.println(godownHead);
            if (Objects.nonNull(godownHead)) {
                // Generate OTP
                String otp = generateOtp();
                otpMap.put(godownheadNo,otp); // Store OTP in map for verification
                otpService.saveOtp(godownheadNo,otp);

                // Send OTP to user via SMS or email (implement this part)

                return ResponseEntity.ok(otp);
            } else {
                return ResponseEntity.badRequest().body("Invalid contact number.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }
    }

    public ResponseEntity<?> resetpassword(String godownheadNo, String newPassword) {
        try {
            GodownHead godownHead = godownHeadRepository.findByContactNumber(godownheadNo);
            if (godownHead != null) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                godownHead.setPassword(hashedPassword);
                godownHeadRepository.save(godownHead);
                return ResponseEntity.ok("Password reset successfully.");
            }
            return ResponseEntity.badRequest().body("Invalid godown head number.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }


    }
    public ResponseEntity<?> loginWithOtp(String godownheadNo, String enteredOtp) {
        try {
            // Check if the OTP is valid
            if (otpService.verifyOtp(godownheadNo, enteredOtp).getStatusCode().equals(HttpStatus.OK)) {
                GodownHead godownHead = godownHeadRepository.findByContactNumber(godownheadNo);
                if (godownHead != null) {
                    // Generate a new token and set the user as authenticated
                    Cookie cookie = generateUserCookie(godownHead.getUsername());
                    authService.createAuthInfo(godownHead.getUsername(), cookie.getValue());

                    Map<String, Object> result = new HashMap<>();
                    result.put("message", "Login with OTP successful.");
                    result.put("cookie", cookie.getValue());
                    result.put("username", godownHead.getUsername());
                    result.put("godownId", godownHead.getGodownId());
                    result.put("godown_head_number", godownHead.getGodownheadNo());
                    result.put("godownHeadId", godownHead.getGodownHeadId());
                    result.put("role", godownHead.getRole().name());

                    // Make the API call here when the status code is 200
                    // Replace "apiCall()" with the actual method to call your API
                    // apiCall();

                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.badRequest().body("Invalid godown head number.");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }
    }



//    public ResponseEntity<?> loginWithOtp(String godownheadNo, String enteredOtp) {
//        try {
//            // Check if the OTP is valid
////            if (otpService.verifyOtp(godownheadNo, enteredOtp)) {
//                GodownHead godownHead = godownHeadRepository.findByContactNumber(godownheadNo);
//                if (godownHead != null &&  otpService.verifyOtp(godownheadNo,enteredOtp ).getStatusCode() == HttpStatusCode.valueOf(200)) {
//                    // Generate a new token and set the user as authenticated
//                    Cookie cookie = generateUserCookie(godownHead.getUsername());
//                    authService.createAuthInfo(godownHead.getUsername(), cookie.getValue());
//
//
//                    Map<String, Object> result = new HashMap<>();
//                    result.put("message", "Login with OTP successful.");
//                    result.put("cookie", cookie.getValue());
//                    result.put("username", godownHead.getUsername());
//                    result.put("godownId", godownHead.getGodownId());
//                    result.put("godown_head_number",godownHead.getGodownheadNo());
//                    result.put("godownHeadId", godownHead.getGodownHeadId());
//                    result.put("role", godownHead.getRole().name());
//                    return ResponseEntity.ok(result);
//                } else {
//                    return ResponseEntity.badRequest().body("Invalid godown head number.");
//                }
////            } else {
////                return ResponseEntity.badRequest().body("Invalid OTP.");
////            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
//        }
//    }
}
