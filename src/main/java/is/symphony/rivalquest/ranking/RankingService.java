package is.symphony.rivalquest.ranking;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RankingService {

    // In real case this should be real database
    private final Map<UUID, UserRanking> rankingMap = new HashMap<>();

    public RankingService() { }

    public void registerGame(UUID playerOne, UUID playerTwo, String result) {
        rankingMap.putIfAbsent(playerOne, new UserRanking(playerOne));
        rankingMap.putIfAbsent(playerTwo, new UserRanking(playerTwo));

        switch (result) {
            case "1/2-1/2" -> {
                rankingMap.get(playerOne).addDraw();
                rankingMap.get(playerTwo).addDraw();
            }
            case "1-0" -> {
                rankingMap.get(playerOne).addWin();
                rankingMap.get(playerTwo).addLoss();
            }
            case "0-1" -> {
                rankingMap.get(playerTwo).addWin();
                rankingMap.get(playerOne).addLoss();
            }
        }
    }

    public List<UserRanking> getRankingList() {
        return rankingMap.values().stream().sorted((o1, o2) -> {
            int compareWins = Long.compare(o2.getWins(), o1.getWins());

            if (compareWins != 0) {
                return compareWins;
            }

            int compareDraws = Long.compare(o2.getDraws(), o1.getDraws());

            if (compareDraws != 0) {
                return compareDraws;
            }

            return Long.compare(o1.getLosses(), o2.getLosses());
        }).toList();
    }
}
