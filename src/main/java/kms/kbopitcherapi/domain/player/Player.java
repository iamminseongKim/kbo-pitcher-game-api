package kms.kbopitcherapi.domain.player;

import jakarta.persistence.*;
import kms.kbopitcherapi.domain.BaseEntity;
import kms.kbopitcherapi.domain.file.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Player extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Team team;

    private String name;

    private Position position;

    private LocalDate birthDate;

    private int backNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Builder
    private Player(File file, int backNumber, LocalDate birthDate, Position position, String name, Team team) {
        this.file = file;
        this.backNumber = backNumber;
        this.birthDate = birthDate;
        this.position = position;
        this.name = name;
        this.team = team;
    }

}
