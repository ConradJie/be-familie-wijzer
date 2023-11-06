package com.jie.befamiliewijzer.exceptions;


public class UnprocessableEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public UnprocessableEntityException() {
        super();
    }
    public UnprocessableEntityException(String message) {
        super(message);
    }
}
