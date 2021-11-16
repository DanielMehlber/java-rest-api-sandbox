package com.danielmehlber.sandbox.db;


import com.danielmehlber.sandbox.db.exceptions.DataBaseManagmentException;

/**
 * this interface defines methods used for database management, such as connection management.
 *
 * {@link DataBaseAccess} uses an implementation of this interface to organize database connections.
 */
public interface DataBaseManager {

    /*
     * connection mgmt
     */

    /**
     * Try to connect to database.
     *
     * @throws DataBaseManagmentException there are many causes for this: connection issues, wrong credentials, etc.
     */
    public void connect() throws DataBaseManagmentException;

    /**
     * Close connection to database
     *
     * @throws DataBaseManagmentException there can be many causes for this, but usually this can be ignored
     */
    public void disconnect() throws DataBaseManagmentException;

}
