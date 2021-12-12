package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

public interface DataBaseUpdater {

    int insertPerson(Person person) throws DataBaseException, InternalErrorException;
    void deletePerson(int id) throws DataBaseException, NoSuchPersonException, InternalErrorException;
    void updatePerson(Person person) throws DataBaseException, NoSuchPersonException, InternalErrorException;


}
