package is.symphony.rivalquest.ranking;

import java.util.UUID;

public class UserRanking {
    private final UUID id;

    private long wins = 0;

    private long draws = 0;

    private long losses = 0;

    public UserRanking(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public long getWins() {
        return wins;
    }

    public void addWin() {
        this.wins += 1;
    }

    public long getDraws() {
        return draws;
    }

    public void addDraw() {
        this.draws += 1;
    }

    public long getLosses() {
        return losses;
    }

    public void addLoss() {
        this.losses += 1;
    }
}
