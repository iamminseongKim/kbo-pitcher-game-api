package kms.kbopitcherapi.domain.file;


import jakarta.persistence.*;
import kms.kbopitcherapi.domain.BaseEntity;
import kms.kbopitcherapi.domain.player.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Files")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String originalFilename;
    private String quizFilename;

}
