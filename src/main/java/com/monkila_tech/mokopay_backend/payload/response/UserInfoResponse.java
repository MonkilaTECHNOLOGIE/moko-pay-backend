package com.monkilatech.madeinrdc.payload.response;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private UUID id;
    private String username;
    private String email;
    private String profil;
    private String phone;
    private Boolean status;
    private List<String> roles;
    private String token;

    public UserInfoResponse(UUID id, String username, String email, Boolean status, List<String> roles, String token,
            String profil, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.roles = roles;
        this.token = token;
        this.profil = profil;
        this.phone = phone;
    }

}
