package kms.kbopitcherapi.domain.player;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    @DisplayName("선수를 생성한다.")
    void makePlayer() throws Exception {
        //given
        Player player = Player.builder()
                .backNumber(1)
                .birthDate(LocalDate.of(1997, 7, 25))
                .position(Position.SP)
                .team(Team.SK)
                .name("김민성")
                .file(null)
                .build();

        //when //then
        assertThat(player.getBackNumber()).isEqualTo(1);
        assertThat(player.getBirthDate()).isEqualTo(LocalDate.of(1997, 7, 25));
        assertThat(player.getPosition()).isEqualTo(Position.SP);
        assertThat(player.getTeam()).isEqualTo(Team.SK);
        assertThat(player.getName()).isEqualTo("김민성");
    }


}