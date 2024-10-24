package kms.kbopitcherapi.api.service.request;

import kms.kbopitcherapi.domain.file.PlayerFile;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlayerCommendServiceRequest {

    private Team team;
    private String name;
    private String originalFilename;
    private String quizFilename;
    private Position position;
    private LocalDate birthDate;
    private int backNumber;

    @Builder
    private PlayerCommendServiceRequest(Team team, String name, String originalFilename, String quizFilename, Position position, LocalDate birthDate, int backNumber) {
        this.team = team;
        this.name = name;
        this.originalFilename = originalFilename;
        this.quizFilename = quizFilename;
        this.position = position;
        this.birthDate = birthDate;
        this.backNumber = backNumber;
    }

    public Player getPlayer() {
        PlayerFile playerFile = PlayerFile.builder()
                .originalFilename(originalFilename)
                .quizFilename(quizFilename)
                .build();

        return Player.builder()
                .name(name)
                .playerFile(playerFile)
                .backNumber(backNumber)
                .birthDate(birthDate)
                .team(team)
                .position(position)
                .build();
    }
}
