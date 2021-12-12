package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;

public interface DataBaseConnection extends DataBaseFetcher, DataBaseUpdater {

    void connect() throws DataBaseException;
    void disconnect() throws DataBaseException;

    default void openTestConnection() throws DataBaseException, InternalErrorException {
        connect();
    }

    default void closeTestConnection() throws DataBaseException, InternalErrorException {
        disconnect();
    }

    default void prepareForTest() throws InternalErrorException {}
    default void cleanupAfterTest() throws DataBaseException, InternalErrorException {}

}
