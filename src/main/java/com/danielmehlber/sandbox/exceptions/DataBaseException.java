package com.danielmehlber.sandbox.exceptions;


/**
 * Exception for all database related issues occurring during interaction with database.
 */
public class DataBaseException extends Exception {

    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(String message, Throwable from) {
        super(message, from);
    }

}
