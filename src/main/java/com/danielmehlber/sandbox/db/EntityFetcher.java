package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.db.exceptions.DataFetchingException;
import com.danielmehlber.sandbox.entities.Person;

/**
 * This interface defines methods for fetching application specific data from a database.
 *
 * This can be accessed via {@link DataBaseAccess}.
 */
public interface EntityFetcher {

    Person fetchPersonById(int id) throws DataFetchingException;

}
