package kms.kbopitcherapi.domain.player;

import jakarta.persistence.*;
import kms.kbopitcherapi.domain.BaseEntity;
import kms.kbopitcherapi.domain.file.PlayerFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Player extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Team team;

    private String name;

    @Enumerated(EnumType.STRING)
    private Position position;

    private LocalDate birthDate;

    private int backNumber;

    @ColumnDefault("'N'")
    private String deleteYn;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_id")
    private PlayerFile playerFile;

    @Builder
    private Player(PlayerFile playerFile, int backNumber, LocalDate birthDate, Position position, String name, Team team) {
        this.playerFile = playerFile;
        this.backNumber = backNumber;
        this.birthDate = birthDate;
        this.position = position;
        this.name = name;
        this.team = team;
        this.deleteYn = "N";
    }

    public Player logicDelete() {
        this.deleteYn = "Y";
        return this;
    }
}
