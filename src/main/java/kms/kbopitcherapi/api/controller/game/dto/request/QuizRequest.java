package kms.kbopitcherapi.api.controller.game.dto.request;

import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import lombok.Getter;

@Getter
public class QuizRequest {

    private Long randomPlayerId;
    private Long userPlayerId;
    private int tryCount;

    public UsersPickPlayerServiceRequest getUsersPickPlayerServiceRequest() {
        return UsersPickPlayerServiceRequest.builder()
                .userPickPlayerId(userPlayerId)
                .randomPlayerId(randomPlayerId)
                .tryCount(tryCount).build();
    }

}
