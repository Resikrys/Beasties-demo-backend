package com.beasties.beasties_backend.shared.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String field) {
        super("Resource already exists: " + field);
    }
}
