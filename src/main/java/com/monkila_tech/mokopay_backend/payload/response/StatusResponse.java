package com.monkilatech.madeinrdc.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;
}
