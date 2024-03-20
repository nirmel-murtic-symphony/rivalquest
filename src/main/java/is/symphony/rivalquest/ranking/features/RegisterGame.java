package is.symphony.rivalquest.ranking.features;

import is.symphony.rivalquest.game.GameAggregate;
import is.symphony.rivalquest.ranking.RankingService;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class RegisterGame {

    // Listening for GameFinishedEvent as external event source
    @EventHandler
    public void on(GameAggregate.GameFinishedEvent event, RankingService rankingService) {
        rankingService.registerGame(event.playerOneId(), event.playerTwoId(), event.result());
    }
}
