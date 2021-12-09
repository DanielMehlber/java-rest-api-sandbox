package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.db.sql.JPADataBase;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;

public class DataBaseAccess {

    private static DataBaseConnection connection;

    static {
        connection = new JPADataBase();
    }

    public static DataBaseConnection getConnection() {
        return connection;
    }

    public static void init() throws InternalErrorException, DataBaseException {
        getConnection().connect();
    }

    public static void cleanUp() {
        // cannot handle any fails (because end of lifecycle)
        try {
            getConnection().disconnect();
        } catch (Exception ignored) {}
    }
}
