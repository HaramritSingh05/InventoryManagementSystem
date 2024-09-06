package com.electrowaveselectronics.inventorymanagement.controller;

import com.electrowaveselectronics.inventorymanagement.service.BillPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@CrossOrigin(origins = "${myapp.cors.origin}", allowCredentials = "true")
@Controller
@RequestMapping("/api")
public class BillPdfController {
    @Autowired
    private BillPdfService billPdfService;
    @GetMapping("/generateBillPdf/{orderId}")
    public ResponseEntity<InputStreamResource> createPdf(@PathVariable int orderId){
try {
    ByteArrayInputStream pdf = billPdfService.createPdf(orderId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-Disposition", "inline;file=lcwd.pdf");
    return ResponseEntity
            .ok()
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(pdf));

}   catch (Exception e) {
    // Handle exceptions here
    e.printStackTrace(); // Example handling (log or throw custom exception)
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.TEXT_PLAIN)
            .body(new InputStreamResource(new ByteArrayInputStream("Error occurred while generating PDF".getBytes())));
}
    }
}
