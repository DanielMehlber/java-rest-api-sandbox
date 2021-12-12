package com.danielmehlber.sandbox.logic;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

public class PersonLogic {

    public static int addPerson(final Person addMe) throws InternalErrorException, DataBaseException {
        return DataBaseAccess.getConnection().insertPerson(addMe);
    }

    public static Person getPersonById(final int id) throws NoSuchPersonException, InternalErrorException, DataBaseException {
        return DataBaseAccess.getConnection().fetchById(id);
    }

}
