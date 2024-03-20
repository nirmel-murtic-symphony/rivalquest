package is.symphony.rivalquest.game.features;

import is.symphony.rivalquest.game.Game;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class GetGame {

    public record GetGameQuery(UUID gameId) { }

    @Bean
    public RouterFunction<ServerResponse> getGameRoute(QueryGateway queryGateway) {
        return route().GET("/games/{gameId}", req -> {
            try {
                var gameId = UUID.fromString(req.pathVariable("gameId"));

                var game = queryGateway.query(new GetGameQuery(gameId), Game.class);

                if (game == null) {
                    return ServerResponse.notFound().build();
                }

                return ServerResponse.ok().body(game);
            }
            catch (IllegalArgumentException e) {
                return ServerResponse.badRequest().body(e.getMessage());
            }
        }).build();
    }
}
