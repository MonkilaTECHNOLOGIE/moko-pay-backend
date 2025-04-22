package com.monkila_tech.mokopay_backend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monkila_tech.mokopay_backend.payload.request.SendMessageRequest;
import com.monkila_tech.mokopay_backend.payload.response.StatusResponse;
import com.monkila_tech.mokopay_backend.services.SmsService;



@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @SuppressWarnings("unused")
    @Autowired
    private SmsService smsService;

    @SuppressWarnings("unused")
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody SendMessageRequest messageRequest) {

        @SuppressWarnings("rawtypes")
        StatusResponse statusResponse = new StatusResponse();

        try {
            String messagePayload = 
                    "Bonjour, Vous avez demandé un code de vérification. \n Voici votre code :"
                     + messageRequest.getCode() +  "." ;

            // smsService.sendSms(messageRequest.getTo(), messagePayload);
            statusResponse.setMessage("Message envoyé avec success au" + messageRequest.getTo());
        } catch (Exception e) {
            statusResponse.setMessage(e.getMessage());
            statusResponse.setStatus(400);
            return ResponseEntity.badRequest().body(statusResponse);
        }

        return ResponseEntity.ok().body(statusResponse);
    }
}
