package com.uniware.integrations.web.exception;

/**
 * @author Abhishek Kumar on 28/11/18.
 */
public class BadRequest extends RuntimeException  {
    private String message;

    public BadRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
