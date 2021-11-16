package com.danielmehlber.sandbox.db.sql;

import com.danielmehlber.sandbox.db.EntityFetcher;
import com.danielmehlber.sandbox.entities.Person;

import java.sql.SQLException;

public class JPAEntityFetcher implements EntityFetcher {

    @Override
    public Person fetchPersonById(int id) {
        return null;
    }
}
