package kms.kbopitcherapi.api.service.request;

import lombok.Builder;
import lombok.Getter;

// TODO : 사용자 선택 플레이어 정보 + 랜덤 플레이어 정보
@Getter
public class UsersPickPlayerServiceRequest {

    private Long userPickPlayerId;
    private Long randomPlayerId;

    private int tryCount;

    public boolean hasUserTryCountOver() {
        return tryCount > 6;
    }

    public boolean userPickIsCorrect() {
        return randomPlayerId.equals(userPickPlayerId);
    }

    public boolean userPickIsWrong() {
        return !userPickIsCorrect();
    }

    @Builder
    private UsersPickPlayerServiceRequest(Long userPickPlayerId, Long randomPlayerId, int tryCount) {
        this.userPickPlayerId = userPickPlayerId;
        this.randomPlayerId = randomPlayerId;
        this.tryCount = tryCount;
    }
}
