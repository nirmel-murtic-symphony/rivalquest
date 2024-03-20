package is.symphony.rivalquest.player;

import java.util.UUID;

public class Player {
    private final UUID id;
    private String name;

    public Player(final UUID id) {
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
