package com.danielmehlber.sandbox.db.sql;

import com.danielmehlber.sandbox.db.DataBaseConnection;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JPADataBase implements DataBaseConnection {

    @PersistenceContext(unitName = "person")
    EntityManager entityManager;

    private boolean applicationManaged = false;

    public JPADataBase() {

    }

    @Override
    public void connect() throws DataBaseException {
        try {
            if (entityManager == null) {
                applicationManaged = true;
                entityManager = Persistence.createEntityManagerFactory("person").createEntityManager();
            }
        } catch (Exception e) {
            throw new DataBaseException("EntityManager creation failed, cannot connect to database", e);
        }
        if(entityManager == null) throw new DataBaseException("EntityManager has not been injected by JavaEE Server");
    }

    @Override
    public void disconnect() throws DataBaseException {
        if(applicationManaged == true)
            entityManager.close();
    }

    @Override
    public void openTestConnection() throws DataBaseException, InternalErrorException {
        Map<String, String> properties = new HashMap<String, String>();

        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306");
        // properties.put("javax.persistence.jdbc.user", "tester");
        // properties.put("javax.persistence.jdbc.password", "");
        // properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

        entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

        if(!entityManager.isOpen()) throw new DataBaseException("Connection cannot be established");
    }

    @Override
    public void prepareForTest() throws InternalErrorException {
        // 1: disconnect from remaining connections
        if(entityManager.isOpen())
            entityManager.close();

        // 2: Connect without database in order to delete and recreate 'test' database
        Map<String, String> properties = new HashMap<String, String>();

        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306");
        entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

        /*
         * native queries need manual transaction management
         */
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DROP DATABASE IF EXISTS test").executeUpdate();
        entityManager.createNativeQuery("CREATE DATABASE test").executeUpdate();
        entityManager.getTransaction().commit();

        entityManager.close();

        // 3: Connect to database 'test' and create schema
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/test");

        entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

        // setup.sql contains database model
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/setup.sql")));
        String setupQuery = "";

        // read contents of setup.sql
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                setupQuery += line + "\n";
            }
        } catch (IOException e) {
            throw new InternalErrorException("cannot read setup.sql", e);
        }

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(setupQuery).executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Person> fetchPersonByName(String name) throws DataBaseException, InternalErrorException {
        return entityManager    .createQuery("SELECT * FROM Person p WHERE p.name=:name", Person.class)
                                .setParameter("name", name)
                                .getResultList();
    }

    @Override
    public Person fetchById(int id) throws DataBaseException, NoSuchPersonException {
        Person person =  entityManager.find(Person.class, id);
        if(person == null)
            throw new NoSuchPersonException(id);
        return person;
    }

    @Override
    public void insertPerson(Person person) throws DataBaseException, InternalErrorException {
        //throw InternalErrorException.NOT_YET_IMPLEMENTED;
        entityManager.persist(person);
    }

    @Override
    public void deletePerson(int id) throws DataBaseException, InternalErrorException {
        throw InternalErrorException.NOT_YET_IMPLEMENTED;
    }

    @Override
    public void updatePerson(Person person) throws DataBaseException, InternalErrorException {
        throw InternalErrorException.NOT_YET_IMPLEMENTED;
    }
}
