package com.electrowaveselectronics.inventorymanagement.service;

import com.electrowaveselectronics.inventorymanagement.entity.Otp;
import com.electrowaveselectronics.inventorymanagement.repository.GodownHeadRepository;
import com.electrowaveselectronics.inventorymanagement.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private GodownHeadRepository godownHeadRepository;

//    public void saveOtp(String godownheadNo, String otpValue) {
//        Otp otp = new Otp(godownheadNo, otpValue);
//        otpRepository.save(otp);
//    }

    public void saveOtp(String godownheadNo, String otpValue) {
        // Check if an OTP entry already exists for the given phone number
        Otp existingOtp = otpRepository.findByGodownheadNo(godownheadNo);
        if (existingOtp != null) {
            // If an OTP entry already exists, update its value
            existingOtp.setOtpValue(otpValue);
            otpRepository.save(existingOtp); // Update existing OTP entry
        } else {
            // If no OTP entry exists, create a new one
            Otp newOtp = new Otp(godownheadNo, otpValue);
            otpRepository.save(newOtp); // Save new OTP entry
        }
    }



    public Otp getOTPByGodownheadNo(String godownheadNo) {
        return otpRepository.findByGodownheadNo(godownheadNo);
    }

//    public ResponseEntity<?> verifyOtp(String godownheadNo, String enteredOtp) {
//        Otp otp = otpRepository.findByGodownheadNoAndOtpValue(godownheadNo, enteredOtp);
//        if (otp != null && otp.getOtpValue().equals(enteredOtp)) {
//            otpRepository.delete(otp); // Remove OTP from database after successful verification
//            return ResponseEntity.ok("OTP verified successfully. You can now reset your password.");
//        }
//        return ResponseEntity.badRequest().body("Incorrect OTP. Please try again.");
//    }

    public ResponseEntity<?> verifyOtp(String godownheadNo, String enteredOtp) {
        System.out.println(godownheadNo);
        System.out.println(enteredOtp);
        try {
            Otp otp = otpRepository.findByGodownheadNoAndOtpValue(godownheadNo, enteredOtp);
            if (otp != null && otp.getOtpValue().equals(enteredOtp)) {
                otpRepository.delete(otp); // Remove OTP from database after successful verification
                return ResponseEntity.ok("OTP verified successfully. You can now reset your password.");
            }
            return ResponseEntity.badRequest().body("Incorrect OTP. Please try again.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong... " + e.getLocalizedMessage());
        }
    }


    // Add other methods as needed
}
