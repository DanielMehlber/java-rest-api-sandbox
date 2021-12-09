package com.danielmehlber.sandbox.test.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddPersonTest {

    @Before
    public void prepare() throws InternalErrorException, DataBaseException {
        DataBaseAccess.getConnection().openTestConnection();
        DataBaseAccess.getConnection().prepareForTest();
    }

    @After
    public void cleanUp() throws InternalErrorException, DataBaseException {
        DataBaseAccess.getConnection().cleanupAfterTest();
        DataBaseAccess.getConnection().closeTestConnection();
    }

    @Test
    public void testAdd() {

    }

}
