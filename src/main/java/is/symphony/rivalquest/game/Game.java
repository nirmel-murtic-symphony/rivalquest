package is.symphony.rivalquest.game;

import java.util.UUID;

public class Game {
    private final UUID gameId;
    private final UUID playerOneId;
    private UUID playerTwoId;
    private String result;

    public Game(final UUID gameId, final UUID playerOneId) {
        this.gameId = gameId;
        this.playerOneId = playerOneId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerOneId() {
        return playerOneId;
    }

    public UUID getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(final UUID playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }
}
