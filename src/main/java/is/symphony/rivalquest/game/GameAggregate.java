package is.symphony.rivalquest.game;

import is.symphony.rivalquest.game.features.CreateGame;
import is.symphony.rivalquest.game.features.FinishGame;
import is.symphony.rivalquest.game.features.JoinGame;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Aggregate
public class GameAggregate {

    @AggregateIdentifier
    private UUID gameId;
    private UUID playerOneId;
    private UUID playerTwoId;
    private String result;
    private final static List<String> VALID_RESULTS = Arrays.asList("1-0", "0-1", "1/2-1/2");

    /* Aggregate Event Sourcing Events */

    public record GameCreatedEvent(UUID gameId, UUID playerId) { }

    public record GameJoinedEvent(UUID gameId, UUID playerId) { }

    public record GameFinishedEvent(UUID gameId, UUID playerOneId, UUID playerTwoId, String result) { }

    public GameAggregate() { }

    @CommandHandler
    public GameAggregate(CreateGame.CreateGameCommand command) {
        AggregateLifecycle.apply(new GameCreatedEvent(
                command.gameId(), command.playerId()));
    }

    @EventSourcingHandler
    public void on(GameCreatedEvent event) {
        this.gameId = event.gameId();
        this.playerOneId = event.playerId();
    }

    @CommandHandler
    public void handle(JoinGame.JoinGameCommand command) {
        if (playerOneId == null) {
            throw new IllegalStateException("Game doesn't exist.");
        }

        if (playerTwoId != null) {
            throw new IllegalStateException("Game is already joined.");
        }

        if (playerOneId.equals(command.playerId())) {
            throw new IllegalStateException("You cannot play game with yourself.");
        }

        AggregateLifecycle.apply(new GameJoinedEvent(gameId, command.playerId()));
    }

    @EventSourcingHandler
    public void on(GameJoinedEvent event) {
        this.playerTwoId = event.playerId();
    }

    @CommandHandler
    public void handle(FinishGame.FinishGameCommand command) {
        if (playerOneId == null || playerTwoId == null) {
            throw new IllegalStateException("Game is not started.");
        }

        if (result != null) {
            throw new IllegalStateException("Game is already finished.");
        }

        if (command.result() == null || !VALID_RESULTS.contains(command.result())) {
            throw new IllegalStateException("Invalid result.");
        }

        AggregateLifecycle.apply(new GameFinishedEvent(gameId, playerOneId, playerTwoId, command.result()));
    }

    @EventSourcingHandler
    public void on(GameFinishedEvent event) {
        this.result = event.result;
    }
}