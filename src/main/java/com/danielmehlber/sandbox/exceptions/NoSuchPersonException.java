package com.danielmehlber.sandbox.exceptions;

import com.danielmehlber.sandbox.entities.Person;

public class NoSuchPersonException extends Exception {

    public NoSuchPersonException(int id) {
        super(String.format("person with id '%d' does not exist", id));
    }

    public NoSuchPersonException(Person person) {
        this(person.getId());
    }

}
