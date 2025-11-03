package com.beasties.beasties_backend.web.exception;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String resource, Object id) {
    super(resource + " not found: " + id);
  }
}
