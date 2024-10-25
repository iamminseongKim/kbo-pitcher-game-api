package kms.kbopitcherapi.api.service.response;

import lombok.Getter;

@Getter
public enum GameStatus {

    CORRECT("정답"),
    WRONG("오답"),
    GAME_OVER("횟수초과"),
    ;

    private String description;

    GameStatus(String description) {
        this.description = description;
    }
}
