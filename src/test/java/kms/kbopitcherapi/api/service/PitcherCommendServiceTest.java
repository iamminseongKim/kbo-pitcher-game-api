package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PitcherCommendServiceTest {

    @Autowired
    private PitcherCommendService pitcherCommendService;

    @Autowired
    private PlayerRepository playerRepository;

    @AfterEach
    void tearDown() {
        playerRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("선수리스트로 선수를 저장한다.")
    void insertPitchers() throws Exception {
        //given
        PlayerCommendServiceRequest playerRequest = getPlayerCommendRequest();
        //when
        pitcherCommendService.insertPitchers(List.of(playerRequest));
        //then
        List<Player> players = playerRepository.findAll();

        Optional<Player> playerOptional = players.stream()
                .filter(player -> player.getName().equals(playerRequest.getName()))
                .findFirst();

        assertThat(playerOptional.isPresent()).isTrue();
        Player player = playerOptional.get();
        assertThat(player.getName()).isEqualTo("류현진");
    }

    private PlayerCommendServiceRequest getPlayerCommendRequest() {
        return PlayerCommendServiceRequest.builder()
                .team(Team.HH)
                .name("류현진")
                .originalFilename("f5a31498-d822-40c4-80c7-eac568545380.mp4")
                .quizFilename("fe8b6a6d-fb7e-443c-a3ae-efa9938e8d18.mp4")
                .position(Position.SP)
                .birthDate(LocalDate.of(1987, 3, 25))
                .backNumber(99)
                .build();
    }
}