package com.adosar.backend.business.exception;

public class IdNotFoundException extends Exception {
    public IdNotFoundException(Integer id, String origin) {
        super(String.format("ID %1$s was not found in %2$s", id, origin));
    }
}
