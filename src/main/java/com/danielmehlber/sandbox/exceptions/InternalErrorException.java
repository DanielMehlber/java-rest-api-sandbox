package com.danielmehlber.sandbox.exceptions;


/**
 * Errors due to developer mistakes or server-side accidents
 */
public class InternalErrorException extends Exception{

    public static final InternalErrorException NOT_YET_IMPLEMENTED = new InternalErrorException("not yet implemented");

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable from) {
        super(message, from);
    }

}
