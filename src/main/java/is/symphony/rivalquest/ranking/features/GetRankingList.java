package is.symphony.rivalquest.ranking.features;

import is.symphony.rivalquest.ranking.RankingService;
import is.symphony.rivalquest.ranking.UserRanking;
import is.symphony.rivalquest.user.User;
import is.symphony.rivalquest.user.features.GetUser;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Collection;
import java.util.UUID;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Component
public class GetRankingList {

    public record GetUserRankingQuery() { }

    public record UserRankingDTO(UUID id, String name, long wins, long draws, long losses) { }

    @Bean
    public RouterFunction<ServerResponse> getRankingRoute(QueryGateway queryGateway) {
        return route().GET("/ranking", req -> {
            var userRankings = queryGateway
                    .query(new GetUserRankingQuery(), ResponseTypes.multipleInstancesOf(UserRanking.class)).join();

            var mapped = userRankings.stream().map(userRanking -> {
                var user = queryGateway
                        .query(new GetUser.GetUserQuery(userRanking.getId()), User.class).join();

                return new UserRankingDTO(
                        userRanking.getId(), user.getName(), userRanking.getWins(),
                        userRanking.getDraws(), userRanking.getLosses());
            }).toList();

            return ServerResponse.ok().body(mapped);
        }).build();
    }

    @QueryHandler
    public Collection<UserRanking> handle(GetUserRankingQuery query, RankingService rankingService) {
        return rankingService.getRankingList();
    }
}
