package com.monkila_tech.mokopay_backend.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private String to;
    private String message;
    private String code;
}
