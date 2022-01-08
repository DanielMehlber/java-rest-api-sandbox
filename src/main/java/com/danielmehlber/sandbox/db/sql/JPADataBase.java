package com.danielmehlber.sandbox.db.sql;

import com.danielmehlber.sandbox.db.DataBaseConnection;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPADataBase implements DataBaseConnection {

    private static final String TEST_DB_NAME = "PersonDB";

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
            if (applicationManaged)
                entityManager.close();
        } catch (RuntimeException e) {
            throw new DataBaseException("cannot disconnect", e);
        }
    }

    private void createSchemaInCurrentDataBase() throws InternalErrorException {
        try {
            // setup.sql contains database model
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/setup.sql")));
            StringBuilder setupQuery = new StringBuilder();

            // read contents of setup.sql
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    setupQuery.append(line).append("\n");
                }
            } catch (IOException e) {
                throw new InternalErrorException("cannot read setup.sql", e);
            }

            /*
             * JPA cannot process the whole script, so we need to execute it statement
             * by statement, also known as batch processing. Statements are separated by ';'.
             */
            String[] scriptBatches = setupQuery.toString().trim().split(";");
            entityManager.getTransaction().begin();
            for(String batch : scriptBatches) {
                entityManager.createNativeQuery(batch).executeUpdate();
            }
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
        entityManager.createNativeQuery("CREATE DATABASE " + TEST_DB_NAME).executeUpdate();
        entityManager.getTransaction().commit();
    }

    private void dropTestDataBaseIfExists() throws RuntimeException{
        /*
         * native queries need manual transaction management
         */
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DROP DATABASE IF EXISTS " + TEST_DB_NAME).executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public void openTestConnection() throws DataBaseException, InternalErrorException {
        try {
            Map<String, String> properties = new HashMap<>();

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
    public void closeTestConnection() throws DataBaseException {
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
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306");
            entityManager = Persistence.createEntityManagerFactory("test", properties).createEntityManager();

            // 3: drop database if it exists (relevant at initial start) and create new one
            dropTestDataBaseIfExists();
            createTestDatabase();
            entityManager.close();

            // 4: Connect to database 'test' and create schema
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/"+TEST_DB_NAME);
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
    public List<Person> fetchPersonByName(String name) throws InternalErrorException {
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
    @Transactional
    public int insertPerson(Person person) throws InternalErrorException {
        //throw InternalErrorException.NOT_YET_IMPLEMENTED;
        int id;
        try {
            /*
             * in order to get the entities id, we have to use transactions (because entities must be flushed)
             */
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();
            id = person.getId();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new InternalErrorException("Cannot persist person", e);
        }

        return id;
    }

    @Override
    public void deletePerson(int id) throws InternalErrorException {
        throw InternalErrorException.NOT_YET_IMPLEMENTED;
    }

    @Override
    public void updatePerson(Person person) throws InternalErrorException {
        throw InternalErrorException.NOT_YET_IMPLEMENTED;
    }
}
