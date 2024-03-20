package is.symphony.rivalquest.user.features;

import is.symphony.rivalquest.user.User;
import is.symphony.rivalquest.user.UserService;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class GetUser {

    public record GetUserQuery(UUID userId) { }

    @Bean
    public RouterFunction<ServerResponse> getUserRoute(QueryGateway queryGateway) {
        return route().GET("/users/{userId}", req -> {
            try {
                var userId = UUID.fromString(req.pathVariable("userId"));

                var user = queryGateway.query(new GetUserQuery(userId), User.class);

                if (user == null) {
                    return ServerResponse.notFound().build();
                }

                return ServerResponse.ok().body(user);
            }
            catch (IllegalArgumentException e) {
                return ServerResponse.badRequest().body(e.getMessage());
            }
        }).build();
    }

    @QueryHandler
    public User handle(GetUserQuery query, UserService userService) {
        return userService.getUser(query.userId());
    }
}
