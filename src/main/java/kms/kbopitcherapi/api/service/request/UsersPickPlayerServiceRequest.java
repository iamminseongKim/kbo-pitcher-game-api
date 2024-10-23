package kms.kbopitcherapi.api.service.request;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

// TODO : 사용자 선택 플레이어 정보 + 랜덤 플레이어 정보
@Getter
public class UsersPickPlayerServiceRequest {

    private Long userPickPlayerId;
    private Long randomPlayerId;

    public boolean userPickIsCurrentAnswer() {
        return Objects.equals(userPickPlayerId, randomPlayerId);
    }
}
