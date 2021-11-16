package com.danielmehlber.sandbox.db.sql;

import com.danielmehlber.sandbox.db.DataBaseManager;
import jdk.jshell.spi.ExecutionControl;

public class JPADataBaseManager implements DataBaseManager {
    @Override
    public void connect() {
        throw new RuntimeException();
    }

    @Override
    public void disconnect() {
        throw new RuntimeException();
    }
}
