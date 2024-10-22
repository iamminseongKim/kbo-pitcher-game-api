package kms.kbopitcherapi.domain.player;

import kms.kbopitcherapi.domain.file.PlayerFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    @DisplayName("선수를 생성한다.")
    void makePlayer() throws Exception {
        //given
        PlayerFile file = PlayerFile.builder()
                .originalFilename("test.mp4")
                .quizFilename("test_q.mp4").build();

        Player player = Player.builder()
                .backNumber(1)
                .birthDate(LocalDate.of(1997, 7, 25))
                .position(Position.SP)
                .team(Team.SK)
                .name("김민성")
                .playerFile(file)
                .build();

        //when //then
        assertThat(player.getBackNumber()).isEqualTo(1);
        assertThat(player.getBirthDate()).isEqualTo(LocalDate.of(1997, 7, 25));
        assertThat(player.getPosition()).isEqualTo(Position.SP);
        assertThat(player.getTeam()).isEqualTo(Team.SK);
        assertThat(player.getName()).isEqualTo("김민성");
        assertThat(player.getPlayerFile()).isEqualTo(file);
        assertThat(player.getDeleteYn()).isEqualTo("N");
    }

    @Test
    @DisplayName("선수를 논리 삭제한다.")
    void logicDelete() throws Exception {
        //given
        Player player = Player.builder()
                .backNumber(1)
                .birthDate(LocalDate.of(1997, 7, 25))
                .position(Position.SP)
                .team(Team.SK)
                .name("김민성")
                .build();

        //when
        Player deletedPlayer = player.logicDelete();
        //then
        assertThat(deletedPlayer).isEqualTo(player);
        assertThat(deletedPlayer.getDeleteYn()).isEqualTo("Y");
    }

}