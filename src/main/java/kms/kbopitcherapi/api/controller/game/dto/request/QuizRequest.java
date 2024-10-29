package kms.kbopitcherapi.api.controller.game.dto.request;

import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizRequest {

    private Long randomPlayerId;
    private Long userPlayerId;
    private int tryCount;

    public UsersPickPlayerServiceRequest toUsersPickPlayerServiceRequest() {
        return UsersPickPlayerServiceRequest.builder()
                .userPickPlayerId(userPlayerId)
                .randomPlayerId(randomPlayerId)
                .tryCount(tryCount)
                .build();
    }

    @Builder
    private QuizRequest(Long randomPlayerId, Long userPlayerId, int tryCount) {
        this.randomPlayerId = randomPlayerId;
        this.userPlayerId = userPlayerId;
        this.tryCount = tryCount;
    }
}
