package com.danielmehlber.sandbox.logic;

import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

public class PersonLogic {

    @PersistenceContext(unitName = "person")
    private static EntityManager persistenceMgr;

    @Resource
    private static UserTransaction transaction;

    public static void addPerson(final Person person)  throws Exception{
        transaction.begin();
        persistenceMgr.persist(person);
        transaction.commit();
    }

    public static void updatePerson(final Person person) throws NoSuchPersonException {
        Person _p = persistenceMgr.find(Person.class, person.getId());
        if (_p == null) throw new NoSuchPersonException(person);
        persistenceMgr.persist(person);
    }

    public static void deletePerson(final int id) throws NoSuchPersonException {
        Person _p = persistenceMgr.find(Person.class, id);
        if (_p == null) throw new NoSuchPersonException(id);
        persistenceMgr.remove(_p);
    }


}
