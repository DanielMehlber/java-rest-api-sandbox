# Rough Edges

## Jersey Test Framework
### You need to mount your API in unit tests
When you want to test your API, you have to mount it in test configuration.

```java
public class AddPersonTest extends JerseyTest {

    /**
     * Required by Jersey Test Framework
     * @return Application Configuration
     */
    @Override
    protected Application configure() {
        // mount PersonAPI
        return new ResourceConfig(RestApplication.class, PersonAPI.class);
    }
```

### API gets mounted on its own path, not the Application path

```java
@ApplicationPath("/api")
public class RestApplication {...}

@Path("/person")
public class PersonAPI {...}

public class Test extends JerseyTest {
    @Override
    protected Application configure() {
        // mount PersonAPI
        return new ResourceConfig(RestApplication.class, PersonAPI.class);
    }
}
```
Take a look at this example. Where will the PersonAPI be mounted?
* at `/api/person`
* at `/person` (right answer)

## JPA
### Native Queries need manual transaction management
Native Queries are non-JPQL Queries and therefor need manuel transaction management.

```java
/*
 * native queries need manual transaction management
 */
entityManager.getTransaction().begin();
entityManager.createNativeQuery("DROP DATABASE IF EXISTS test").executeUpdate();
entityManager.getTransaction().commit();
```

## Jackson for JSON (de-)serialization
### `@JsonProperty` does not work on attribures
Sometimes there are required attributes when it comes to deserializing JSON. 
Normally you would mark them as required by using the `@JsonProperty` annotation.

```java
import com.fasterxml.jackson.annotation.JsonProperty;

class Wrong {
    @JsonProperty(value = "wrong", required = true)
    public String wrong;
}
```

But this does not work!!! You instead have to define a `@JsonCreator` constructor
and use the annotation there.

```java
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class Right {
    public String right;

    @JsonCreator
    public Right(@JsonProperty(value = "right", required = true) String right) {
        this.right = right;
    }
}
```