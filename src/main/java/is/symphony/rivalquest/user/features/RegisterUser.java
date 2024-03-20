package is.symphony.rivalquest.user.features;

import is.symphony.rivalquest.user.UserService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class RegisterUser {

    record Request(String name) { }

    public record RegisterUserCommand(String name) { }

    record Response(UUID userId) { }

    @Bean
    public RouterFunction<ServerResponse> registerUserRoute(CommandGateway commandGateway) {
        return route().POST("/users", req -> {
            var request = req.body(Request.class);

            UUID userId = commandGateway.sendAndWait(new RegisterUserCommand(request.name()));

            return ServerResponse.ok().body(new Response(userId));
        }).build();
    }

    @CommandHandler
    public UUID handle(RegisterUserCommand command, UserService userService) {
        return userService.addNewUser(command.name());
    }
}
