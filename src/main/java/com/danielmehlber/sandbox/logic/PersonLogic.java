package com.danielmehlber.sandbox.logic;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;

public class PersonLogic {

    public static void addPerson(final Person addMe) throws InternalErrorException, DataBaseException {
        DataBaseAccess.getConnection().insertPerson(addMe);
    }

}
