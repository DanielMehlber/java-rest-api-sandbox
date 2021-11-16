package com.danielmehlber.sandbox.db.exceptions;

/**
 * This exception captures exceptions in the {@link com.danielmehlber.sandbox.db.EntityFetcher} class.
 */
public class DataFetchingException extends Exception {

    public DataFetchingException(String message, Throwable cause) {
        super(message, cause);
    }

}
