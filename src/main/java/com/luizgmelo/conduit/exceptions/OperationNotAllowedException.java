package com.luizgmelo.conduit.exceptions;

public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException() {
        super("Action was not allowed");
    }
}
