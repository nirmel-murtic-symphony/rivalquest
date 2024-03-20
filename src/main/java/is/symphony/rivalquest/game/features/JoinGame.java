package is.symphony.rivalquest.game.features;

import is.symphony.rivalquest.user.User;
import is.symphony.rivalquest.user.features.GetUser;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class JoinGame {

    public record JoinGameCommand(@TargetAggregateIdentifier UUID gameId, UUID playerId) { }

    @Bean
    public RouterFunction<ServerResponse> joinGameRoute(CommandGateway commandGateway, QueryGateway queryGateway) {
        return route().POST("/games/{gameId}/join", req -> {
            try {
                var userId = UUID.fromString(req.headers().header("User").getFirst());

                var user = queryGateway.query(new GetUser.GetUserQuery(userId), User.class).join();

                if (user == null) {
                    return ServerResponse.badRequest().body("User doesn't exist");
                }

                var gameId = UUID.fromString(req.pathVariable("gameId"));

                commandGateway.sendAndWait(new JoinGameCommand(gameId, userId));

                return ServerResponse.noContent().build();
            }
            catch (IllegalStateException | IllegalArgumentException e) {
                return ServerResponse.badRequest().body(e.getMessage());
            }
        }).build();
    }
}
