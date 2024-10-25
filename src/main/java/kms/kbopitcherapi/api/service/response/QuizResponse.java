package kms.kbopitcherapi.api.service.response;

import lombok.Builder;
import lombok.Getter;

// TODO 사용자 선택 값 + 랜덤 값 비교해서 정답 / 오답 구분 응답 객체
@Getter
public class QuizResponse {

    private Long usersPickPitcherId;
    private Long randomPitcherId;

    private GameStatus gameStatus;

    private boolean teamDiff;
    private boolean positionDiff;
    private boolean birthDiff;
    private boolean backNumDiff;

    private int tryCount;


    private PitcherResponse randomPitcherResponse;
    private PitcherResponse userPitcherResponse;

    @Builder
    private QuizResponse(Long usersPickPitcherId, Long randomPitcherId, GameStatus gameStatus, boolean teamDiff, boolean positionDiff, boolean birthDiff, boolean backNumDiff, int tryCount, PitcherResponse randomPitcherResponse, PitcherResponse userPitcherResponse) {
        this.usersPickPitcherId = usersPickPitcherId;
        this.randomPitcherId = randomPitcherId;
        this.gameStatus = gameStatus;
        this.teamDiff = teamDiff;
        this.positionDiff = positionDiff;
        this.birthDiff = birthDiff;
        this.backNumDiff = backNumDiff;
        this.tryCount = tryCount;
        this.randomPitcherResponse = randomPitcherResponse;
        this.userPitcherResponse = userPitcherResponse;
    }
}
