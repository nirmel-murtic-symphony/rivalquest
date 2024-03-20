package is.symphony.rivalquest.game.features;

import is.symphony.rivalquest.game.Game;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class GetAllGames {

    public record GetAllGamesQuery() { }

    @Bean
    public RouterFunction<ServerResponse> getAllGamesRoute(QueryGateway queryGateway) {
        return route().GET("/games", req -> {
            var games = queryGateway.query(new GetAllGamesQuery(), ResponseTypes.multipleInstancesOf(Game.class));

            return ServerResponse.ok().body(games);
        }).build();
    }
}
