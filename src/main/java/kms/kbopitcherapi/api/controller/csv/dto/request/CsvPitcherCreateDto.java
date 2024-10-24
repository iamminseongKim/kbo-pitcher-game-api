package kms.kbopitcherapi.api.controller.csv.dto.request;

import kms.kbopitcherapi.api.controller.csv.exception.NotFoundAtMakePlayerException;
import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record CsvPitcherCreateDto(
        String team,
        String name,
        String originalFileName,
        String quizFileName,
        String position,
        String birthDate,
        String backNumber
) {

    public PlayerCommendServiceRequest toPlayerCommendServiceRequest() {
        return PlayerCommendServiceRequest.builder()
                .name(name)
                .team(Team.findTeam(team))
                .birthDate(parseKoreanDate(birthDate))
                .position(Position.findPosition(position))
                .originalFilename(originalFileName)
                .quizFilename(quizFileName)
                .backNumber(getBackNumber())
                .build();
    }

    private int getBackNumber() {
        try {
            return Integer.parseInt(backNumber);
        } catch (NumberFormatException e) {
            throw new NotFoundAtMakePlayerException("선수의 등번호가 잘못됬습니다. backNumber: " + backNumber );
        }
    }

    private LocalDate parseKoreanDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        try {
            return LocalDate.parse(birthDate, formatter);
        } catch (DateTimeParseException e) {
            throw new NotFoundAtMakePlayerException("날짜 형식이 잘못됐습니다. : " + birthDate);
        }

    }

}
