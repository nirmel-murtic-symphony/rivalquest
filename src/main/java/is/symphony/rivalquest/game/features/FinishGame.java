package is.symphony.rivalquest.game.features;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class FinishGame {

    record Request(String result) { }

    public record FinishGameCommand(@TargetAggregateIdentifier UUID gameId, String result) { }

    @Bean
    public RouterFunction<ServerResponse> finishGameRoute(CommandGateway commandGateway) {
        return route().POST("/games/{gameId}/finish", req -> {
            try {
                var request = req.body(Request.class);

                var gameId = UUID.fromString(req.pathVariable("gameId"));

                // In real scenario, this should be dispatched by system in one moment
                commandGateway.sendAndWait(new FinishGameCommand(gameId, request.result));

                return ServerResponse.noContent().build();
            }
            catch (IllegalStateException | IllegalArgumentException e) {
                return ServerResponse.badRequest().body(e.getMessage());
            }
        }).build();
    }
}
