package com.adosar.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ExceptionResponseMessage {

    private Instant time;
    private int status;
    private String error;
    private String exception;
    private String message;
}
