package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

import java.util.List;

public interface DataBaseFetcher {

    /**
     * fetches person with equal name
     * @param name of person
     * @return List of people of same name
     * @throws DataBaseException errors while interacting with DB e.g. connection issues, etc
     * @throws InternalErrorException errors due to developer mistakes or internal accidents
     */
    List<Person> fetchPersonByName(String name) throws DataBaseException, InternalErrorException;


    Person fetchById(int id) throws DataBaseException, InternalErrorException, NoSuchPersonException;

}
