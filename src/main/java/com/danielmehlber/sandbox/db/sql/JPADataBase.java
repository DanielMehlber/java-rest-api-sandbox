package com.danielmehlber.sandbox.db.sql;

import com.danielmehlber.sandbox.db.DataBaseConnection;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            if (applicationManaged == true)
                entityManager.close();
        } catch (RuntimeException e) {
            throw new DataBaseException("cannot disconnect", e);
        }
    }

    private void createSchemaInCurrentDataBase() throws InternalErrorException {
        try {
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
        } catch (RuntimeException e) {
            throw new InternalErrorException("cannot create schema on database", e);
        }
    }

    private void createTestDatabase() throws RuntimeException{
        /*
         * native queries need manual transaction management
         */
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("CREATE DATABASE test").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private void dropTestDataBaseIfExists() throws RuntimeException{
        /*
         * native queries need manual transaction management
         */
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DROP DATABASE IF EXISTS test").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public void openTestConnection() throws DataBaseException, InternalErrorException {
        try {
            Map<String, String> properties = new HashMap<String, String>();

            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306");
            // properties.put("javax.persistence.jdbc.user", "tester");
            // properties.put("javax.persistence.jdbc.password", "");
            // properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

            entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

            if (!entityManager.isOpen()) throw new DataBaseException("Connection cannot be established");
        } catch (RuntimeException e) {
            throw new InternalErrorException("Cannot open test connection", e);
        }
    }

    @Override
    public void closeTestConnection() throws DataBaseException, InternalErrorException {
        try {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        } catch (RuntimeException e) {
            throw new DataBaseException("Cannot close test connection", e);
        }
    }

    @Override
    public void prepareForTest() throws InternalErrorException {
        try {
            // 1: disconnect from remaining connections
            if (entityManager.isOpen())
                entityManager.close();

            // 2: Connect without database in order to delete and recreate 'test' database
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306");
            entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

            // 3: drop database if it exists (relevant at initial start) and create new one
            dropTestDataBaseIfExists();
            createTestDatabase();
            entityManager.close();

            // 4: Connect to database 'test' and create schema
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/test");
            entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

            // load setup.sql and create schema in current database (which is the newly created 'test' database)
            createSchemaInCurrentDataBase();
        } catch (RuntimeException e) {
            throw new InternalErrorException("Cannot prepare for test", e);
        }
    }

    @Override
    public void cleanupAfterTest() throws DataBaseException, InternalErrorException {
        try {
            dropTestDataBaseIfExists();
            closeTestConnection();
        } catch (RuntimeException e) {
            throw new InternalErrorException("cannot clean up after test", e);
        }
    }

    @Override
    public List<Person> fetchPersonByName(String name) throws DataBaseException, InternalErrorException {
        try {
            return entityManager.createQuery("SELECT * FROM Person p WHERE p.name=:name", Person.class)
                    .setParameter("name", name)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new InternalErrorException("Cannot fetch person by name", e);
        }
    }

    @Override
    public Person fetchById(int id) throws DataBaseException, NoSuchPersonException {
        try {
            Person person = entityManager.find(Person.class, id);
            if (person == null)
                throw new NoSuchPersonException(id);
            return person;
        } catch (RuntimeException e) {
            throw new DataBaseException("cannot fetch person by id", e);
        }
    }

    @Override
    public void insertPerson(Person person) throws DataBaseException, InternalErrorException {
        //throw InternalErrorException.NOT_YET_IMPLEMENTED;
        try {
            entityManager.persist(person);
        } catch (RuntimeException e) {
            throw new InternalErrorException("Cannot persist person", e);
        }
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
