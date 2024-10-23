package kms.kbopitcherapi.api.service.response;

import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PitcherResponse {

    private Long id;
    private String name;
    private Team team;
    private Position position;
    private LocalDate birthDate;
    private int backNumber;
    private String quizFileName;
    private String answerFileName;

    @Builder
    private PitcherResponse(Long id, String name, Team team, Position position, LocalDate birthDate, int backNumber, String quizFileName, String answerFileName) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.position = position;
        this.birthDate = birthDate;
        this.backNumber = backNumber;
        this.quizFileName = quizFileName;
        this.answerFileName = answerFileName;
    }



    public static PitcherResponse of(Player player) {
        return PitcherResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .team(player.getTeam())
                .position(player.getPosition())
                .backNumber(player.getBackNumber())
                .birthDate(player.getBirthDate())
                .quizFileName(player.getPlayerFile().getQuizFilename())
                .answerFileName(player.getPlayerFile().getOriginalFilename())
                .build();
    }
}
