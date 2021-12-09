package com.danielmehlber.sandbox.exceptions;


/**
 * errors due to data that already exists and therefore cannot be created.
 */
public class AlreadyExistsException extends Exception {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable from) {
        super(message, from);
    }

}
