package com.monkilatech.madeinrdc.payload.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Otp {
    private String otp;
    private String phoneNumber;
}
