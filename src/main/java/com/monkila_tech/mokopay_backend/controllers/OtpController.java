package com.monkilatech.madeinrdc.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.monkilatech.madeinrdc.payload.request.SendMailRequest;
import com.monkilatech.madeinrdc.payload.response.StatusResponse;
import com.monkilatech.madeinrdc.services.OtpService;
import com.monkilatech.madeinrdc.utils.ValueException;

@RestController
@RequestMapping("/api/auth")
public class OtpController {


    @Autowired
    public OtpService otpService;

    @Value("${apikey-firebase}")
    private static String FIREBASE_API_KEY;
    
    private static final String SEND_OTP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendVerificationCode?key=" + FIREBASE_API_KEY;
    private static final String VERIFY_OTP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPhoneNumber?key=" + FIREBASE_API_KEY;

    @GetMapping("/sendOtp")
    public String sendOtp(@RequestBody SendMailRequest sendMailRequest) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = new HashMap<>();
        request.put("phoneNumber", sendMailRequest.getPhone());
        // request.put("recaptchaToken", "YOUR_RECAPTCHA_TOKEN");  // Optionnel

        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.postForEntity(SEND_OTP_URL, request, Map.class);
        return response.getBody().get("sessionInfo").toString(); 
    }

    @GetMapping("/verifyOtp")
    public String verifyOtp(@RequestBody SendMailRequest sendMailRequest) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = new HashMap<>();
        request.put("sessionInfo", sendMailRequest.getUsername());
        request.put("code", Integer.toString(sendMailRequest.getOtpCode()));

        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_OTP_URL, request, Map.class);
        if (response.getBody().containsKey("idToken")) {
            return "Authentification réussie, Token: " + response.getBody().get("idToken");
        } else {
            return "Erreur d'authentification";
        }
    }

    
    @SuppressWarnings("rawtypes")
    @GetMapping("/generateOtp")
    public ResponseEntity generateOTP(@RequestBody SendMailRequest sendMailRequest)
            throws ValueException {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            int otp = otpService.generateOTP(sendMailRequest.getEmail());
            if (otp > 0) {
                
                otpService.sendOtp(sendMailRequest.getEmail(), Integer.toString(otp));

                statusResponse.setStatus(200);
                statusResponse.setMessage("Success");
                statusResponse.setData(otp);
                return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
            } else
                statusResponse.setMessage("Erreur de generation de l'OTP");

        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
            e.printStackTrace();
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/validateOtp")
    public ResponseEntity validateOTP(@RequestBody SendMailRequest sendMailRequest)
            throws ValueException {

        StatusResponse statusResponse = new StatusResponse();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            int otpGet = otpService.getOtp(sendMailRequest.getEmail());
            if (otpGet == sendMailRequest.getOtpCode()) {
                otpService.clearOTP(sendMailRequest.getEmail());
                statusResponse.setStatus(200);
                statusResponse.setMessage("OTP Valide");
                return ResponseEntity.status(HttpStatus.OK).body(statusResponse);
            } else
                statusResponse.setMessage("OTP invalide");

        } catch (Exception e) {
            statusResponse.setStatus(400);
            statusResponse.setMessage("Erreur interne");
            e.printStackTrace();
        }
        return ResponseEntity.status(httpStatus).body(statusResponse);
    }
}
