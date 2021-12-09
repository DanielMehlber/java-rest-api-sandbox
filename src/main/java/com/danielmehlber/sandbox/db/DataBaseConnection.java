package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;

import javax.xml.crypto.Data;

public interface DataBaseConnection extends DataBaseFetcher, DataBaseUpdater {

    void connect() throws DataBaseException, InternalErrorException;
    void disconnect() throws DataBaseException, InternalErrorException;

    default void openTestConnection() throws DataBaseException, InternalErrorException {
        connect();
    }

    default void closeTestConnection() throws DataBaseException, InternalErrorException {
        disconnect();
    }

    default void prepareForTest() throws DataBaseException, InternalErrorException {}
    default void cleanupAfterTest() throws DataBaseException, InternalErrorException {}

}
