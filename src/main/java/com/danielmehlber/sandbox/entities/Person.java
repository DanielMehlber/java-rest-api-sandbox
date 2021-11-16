package com.danielmehlber.sandbox.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="people")
public class Person {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private int age;

    @Column
    private String phrase;

    public Person(int id, String firstname, String lastname, int age, String phrase) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.phrase = phrase;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public boolean isAlive() {
        return age > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && age == person.age && firstname.equals(person.firstname) && lastname.equals(person.lastname) && Objects.equals(phrase, person.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, age, phrase);
    }
}
