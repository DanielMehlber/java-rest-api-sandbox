CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(20) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    age INT NOT NULL,
    phrase TEXT NOT NULL
);