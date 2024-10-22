package kms.kbopitcherapi.domain.file;

import jakarta.persistence.*;
import kms.kbopitcherapi.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlayerFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String originalFilename;
    private String quizFilename;

    @Builder
    private PlayerFile(String originalFilename, String quizFilename) {
        this.originalFilename = originalFilename;
        this.quizFilename = quizFilename;
    }
}
