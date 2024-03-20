package is.symphony.rivalquest.user;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserService {

    // In real case this should be real database
    private final Map<UUID, User> users = new HashMap<>();

    public UserService() { }

    public UUID addNewUser(String name) {
        var userId = UUID.randomUUID();

        var newUser = new User(userId);
        newUser.setName(name);

        users.put(userId, newUser);

        return userId;
    }

    public User getUser(UUID userId) {
        return users.get(userId);
    }
}
