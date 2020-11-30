package com.skcc.modern.pattern.example.patternlabs01example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

  private static final long serialVersionUID = 2277807213304996378L;

  public ResourceNotFoundException(Throwable cause) {
      super(cause);
    }

    public ResourceNotFoundException(final String message) {
    super(message);
  }
  
  public ResourceNotFoundException(final String message, final Throwable t) {
    super(message, t);
  }
}
