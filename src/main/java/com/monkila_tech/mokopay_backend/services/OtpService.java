package com.monkila_tech.mokopay_backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Integer EXPIRE_MINS = 60;
    private LoadingCache<String, Integer> otpCache;

    public OtpService() {
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    
            helper.setFrom("providermail30@gmail.com", "MadeInDRC");
            helper.setTo(toEmail);
            helper.setSubject("Code de vérification - MadeInDRC");
    
            
            String body = "<!DOCTYPE html>"
                    + "<html><body style='font-family:Arial,sans-serif;'>"
                    + "<p>Bonjour,</p>"
                    + "<p>Vous avez demandé un code de vérification.</p>"
                    + " <p>Voici votre code : <span style='font-size:20px; font-weight:bold; color:#2F80ED;'>" + otp + "</span></p>" 
                    + "<p>Ce code est valable pendant <strong>10 minutes</strong>.</p>"
                    + "<br><p>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer ce message.</p>"
                    + "<hr>"
                    + "<p style='font-size:12px;color:gray;'>MadeInDRC - Plateforme numérique de confiance</p>"
                    + "</body></html>";
    
            helper.setText(body, true);
    
            message.addHeader("List-Unsubscribe", "<mailto:providermail30@gmail.com>");
    
            mailSender.send(message);
    
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
}
