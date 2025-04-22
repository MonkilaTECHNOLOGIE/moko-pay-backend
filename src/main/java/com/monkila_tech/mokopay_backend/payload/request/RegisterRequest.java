package com.monkila_tech.mokopay_backend.payload.request;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Set<String> role;
    private String profil;
    private String phone;
    private Boolean status;
}
