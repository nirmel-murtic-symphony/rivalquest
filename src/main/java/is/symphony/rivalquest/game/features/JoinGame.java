package is.symphony.rivalquest.game.features;

import is.symphony.rivalquest.game.GameType;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import is.symphony.rivalquest.game.GameAggregate.*;

@Component
public class CreateGame {

    record Request(GameType gameType) { }

    record Response(UUID gameId) { }

    @Bean
    public RouterFunction<ServerResponse> createGameRoute(CommandGateway commandGateway) {
        return route().POST("/games", req -> {
            Request request = req.body(Request.class);

            UUID userId = UUID.fromString(req.headers().header("User").getFirst());

            UUID gameId = commandGateway.sendAndWait(
                    new CreateGameCommand(
                            UUID.randomUUID(), userId, request.gameType()));

            return ServerResponse.ok().body(new Response(gameId));
        }).build();
    }
}
