package is.symphony.rivalquest.game.features;

import is.symphony.rivalquest.game.Game;
import is.symphony.rivalquest.game.GameProjection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class GetGame {
    @Bean
    public RouterFunction<ServerResponse> getGameRoute(GameProjection gameProjection) {
        return route().GET("/games/{gameId}", req -> {
            try {
                var gameId = UUID.fromString(req.pathVariable("gameId"));

                Game game = gameProjection.getGame(gameId);

                if (game == null) {
                    return ServerResponse.notFound().build();
                }

                return ServerResponse.ok().body(gameProjection.getGame(gameId));
            }
            catch (IllegalArgumentException e) {
                return ServerResponse.badRequest().body(e.getMessage());
            }
        }).build();
    }
}
