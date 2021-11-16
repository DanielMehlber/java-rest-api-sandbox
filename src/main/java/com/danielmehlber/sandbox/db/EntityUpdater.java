package com.danielmehlber.sandbox.db;


import com.danielmehlber.sandbox.db.exceptions.DataUpdateException;
import com.danielmehlber.sandbox.entities.Person;

/**
 * This interface declares methods for persisting applications-specific data.
 *
 * It can be accessed via {@link DataBaseAccess}.
 */
public interface EntityUpdater {

    void createPerson(Person person) throws DataUpdateException;

}
