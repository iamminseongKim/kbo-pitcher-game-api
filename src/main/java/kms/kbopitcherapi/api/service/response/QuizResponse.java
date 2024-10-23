package kms.kbopitcherapi.api.service.response;

import lombok.Getter;

import java.time.LocalDate;

// TODO 사용자 선택 값 + 랜덤 값 비교해서 정답 / 오답 구분 응답 객체
@Getter
public class QuizResponse {

    private Long usersPickPitcherId;
    private Long randomPitcherId;

    private boolean correctAnswer;

    private boolean teamDiff;
    private boolean positionDiff;
    private boolean birthDiff;
    private boolean backNumDiff;


    private PitcherResponse randomPitcherResponse;
    private PitcherResponse userPitcherResponse;
}
