package kms.kbopitcherapi.api.service.response;

import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter
public class PitcherResponse {

    private Long id;
    private String name;
    private String team;
    private String position;
    private int age;
    private int backNumber;
    private String quizFileName;
    private String answerFileName;

    @Builder
    private PitcherResponse(Long id, String name, Team team, Position position, LocalDate birthDate, int backNumber, String quizFileName, String answerFileName) {
        this.id = id;
        this.name = name;
        this.team = team.getTeamName();
        this.position = position.getDescription();
        this.age = calculateExactAge(birthDate, LocalDate.now());
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

    public static PitcherResponse ofRandom(Player player) {
        return PitcherResponse.builder()
                .id(player.getId())
                .team(Team.SK)
                .position(Position.SP)
                .birthDate(LocalDate.now())
                .quizFileName(player.getPlayerFile().getQuizFilename())
                .answerFileName(player.getPlayerFile().getOriginalFilename())
                .build();
    }

    private int calculateExactAge(LocalDate birthDate, LocalDate now) {

        Period period = Period.between(birthDate, now);

        int age = period.getYears();

        if (now.getMonthValue() < birthDate.getMonthValue() ||
                (now.getMonthValue() == birthDate.getMonthValue() && now.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return age;
    }
}
