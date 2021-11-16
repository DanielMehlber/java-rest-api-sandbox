package com.danielmehlber.sandbox.db;

import com.danielmehlber.sandbox.db.sql.JPADataBaseManager;
import com.danielmehlber.sandbox.db.sql.JPAEntityFetcher;
import com.danielmehlber.sandbox.db.sql.JPAEntityUpdater;

/**
 * Enables centralized access to interface implementations necessary for database transactions.
 *
 * This enables an easy interchangeability of databases.
 * Thought behind this: currently using JPA (=SQL), might use REDIS later...
 */
public class DataBaseAccess {

    private static EntityUpdater updater;
    private static EntityFetcher fetcher;
    private static DataBaseManager manager;

    static {
        /*
         * define database implementation here
         */
        updater = new JPAEntityUpdater();
        fetcher = new JPAEntityFetcher();
        manager = new JPADataBaseManager();
    }

    public static EntityUpdater getUpdater() {
        return updater;
    }

    public static void setUpdater(EntityUpdater updater) {
        DataBaseAccess.updater = updater;
    }

    public static EntityFetcher getFetcher() {
        return fetcher;
    }

    public static void setFetcher(EntityFetcher fetcher) {
        DataBaseAccess.fetcher = fetcher;
    }

    public static void setManager(DataBaseManager manager) {
        DataBaseAccess.manager = manager;
    }

    public static DataBaseManager getManager() {
        return manager;
    }
}
