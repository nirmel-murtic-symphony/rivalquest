package is.symphony.rivalquest.user;

import java.util.UUID;

public class User {
    private final UUID id;
    private String name;

    public User(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
