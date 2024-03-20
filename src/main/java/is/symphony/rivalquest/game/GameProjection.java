package is.symphony.rivalquest.game;

import is.symphony.rivalquest.game.features.GetAllGames;
import is.symphony.rivalquest.game.features.GetGame;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import is.symphony.rivalquest.game.GameAggregate.*;

@Component
public class GameProjection {

    // In real case this should be real database
    private final Map<UUID, Game> games = new HashMap<>();

    @EventHandler
    public void on(GameCreatedEvent event) {
        games.put(event.gameId(), new Game(event.gameId(), event.playerId()));
    }

    @EventHandler
    public void on(GameJoinedEvent event) {
        games.computeIfPresent(event.gameId(), (gameId, game) -> {
            game.setPlayerTwoId(event.playerId());

            return game;
        });
    }

    @EventHandler
    public void on(GameFinishedEvent event) {
        games.computeIfPresent(event.gameId(), (gameId, game) -> {
            game.setResult(event.result());

            return game;
        });
    }

    @QueryHandler
    public List<Game> handle(GetAllGames.GetAllGamesQuery query) {
        return games.values().stream().toList();
    }

    @QueryHandler
    public Game handle(GetGame.GetGameQuery query) {
        return games.get(query.gameId());
    }
}
