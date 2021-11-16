package com.danielmehlber.sandbox.db.exceptions;

/**
 * This exception type captures exceptions in the {@link com.danielmehlber.sandbox.db.DataBaseManager} class.
 */
public class DataBaseManagmentException extends Exception {

    public DataBaseManagmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
