package com.danielmehlber.sandbox.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Person() {

    }

    @JsonCreator
    public Person(@JsonProperty("id") int id,
                  @JsonProperty(value="firstname", required=true) String firstname,
                  @JsonProperty(value="lastname", required=true) String lastname,
                  @JsonProperty(value="age", required=true) int age,
                  @JsonProperty(value="phrase", required=true) String phrase) {
        this(firstname, lastname, age, phrase);
        this.id = id;
    }

    public Person(String firstname, String lastname, int age, String phrase) {
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

    @JsonIgnore
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

    /**
     * Does compare two objects of class person, but without comparing their ID
     * @param other other person
     * @return true if content of objects (without ID) are equal
     */
    public boolean equalsInContent(final Person other) {
        if(this == other) return true;
        if(other == null) return false;
        return other.age == age
                && other.firstname.equals(firstname)
                && other.lastname.equals(lastname)
                && other.phrase.equals(phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, age, phrase);
    }
}
