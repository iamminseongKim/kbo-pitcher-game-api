package kms.kbopitcherapi.domain.player.repository;

import kms.kbopitcherapi.IntegrationTestSupport;
import kms.kbopitcherapi.domain.file.PlayerFile;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PlayerRepository playerRepository;

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    @DisplayName("선수를 저장한다.")
    void save() throws Exception {
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
        //when
        Player savedPlayer = playerRepository.save(player);
        //then
        assertThat(savedPlayer).isEqualTo(player);
    }

    @Test
    @DisplayName("선수 이름으로 선수를 조회한다.")
    void findPlayerFromPlayerName() throws Exception {
        //given
        Player player = Player.builder()
                .backNumber(1)
                .birthDate(LocalDate.of(1997, 7, 25))
                .position(Position.SP)
                .team(Team.SK)
                .name("김민성")
                .build();
        playerRepository.save(player);

        //when
        Player findPlayer = playerRepository.findByName("김민성");
        //then
        assertThat(findPlayer.getName()).isEqualTo(player.getName());
        assertThat(findPlayer.getBirthDate()).isEqualTo(player.getBirthDate());
    }

    @Test
    @DisplayName("한글자 검색 시 해당 글자 포함된 리스트를 받는다.")
    void findByNameContaining() throws Exception {
        //given
        Player player1 = Player.builder().name("김민성").build();
        Player player2 = Player.builder().name("김이성").build();
        Player player3 = Player.builder().name("김삼성").build();
        Player player4 = Player.builder().name("이삼성").build();

        playerRepository.saveAll(List.of(player1, player2, player3, player4));

        //when
        List<Player> playerList = playerRepository.findByNameContaining("이");
        //then
        assertThat(playerList).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "김이성",
                        "이삼성"
                );
    }

    @Test
    @DisplayName("사용자가 선택한 플레이어를 검색한다. 동명이인을 방지하기 위해 팀, 이름, 생년월일로 검색한다.")
    void findByNameAndTeamAndBirthDate() throws Exception {
        //given
        Player player1 = Player.builder().name("김민성").team(Team.SK).birthDate(LocalDate.of(1997, 7, 25)).build();
        Player player2 = Player.builder().name("김민성").team(Team.HH).birthDate(LocalDate.of(1997, 7, 25)).build();
        Player player3 = Player.builder().name("김민성").team(Team.SK).birthDate(LocalDate.of(1995, 7, 25)).build();
        Player player4 = Player.builder().name("김이성").team(Team.SK).birthDate(LocalDate.of(1997, 7, 25)).build();

        playerRepository.saveAll(List.of(player1, player2, player3, player4));

        //when
        Player findPlayer = playerRepository.findByNameAndTeamAndBirthDate("김민성", Team.SK, LocalDate.of(1997, 7, 25));
        //then
        assertThat(findPlayer.getName()).isEqualTo(player1.getName());
        assertThat(findPlayer.getBirthDate()).isEqualTo(player1.getBirthDate());
        assertThat(findPlayer.getTeam()).isEqualTo(Team.SK);

    }

    @Test
    @DisplayName("랜덤 선수를 조회한다. 1명만 넣어서 1명만 조회.")
    void findPlayerByRandom() throws Exception {
        //given
        PlayerFile playerFile = PlayerFile.builder()
                .quizFilename("q_kms.mp4")
                .originalFilename("kms.mp4")
                .build();
        Player randomPlayer = Player.builder()
                .name("김민성")
                .team(Team.SK)
                .birthDate(LocalDate.of(1997, 7, 25))
                .playerFile(playerFile)
                .build();

        playerRepository.save(randomPlayer);
        //when

        Optional<Player> playerByRandomOpt = playerRepository.findPlayerByRandom();

        //then
        assertThat(playerByRandomOpt).isPresent();
        Player playerByRandom = playerByRandomOpt.get();
        assertThat(playerByRandom.getName()).isEqualTo(randomPlayer.getName());
        assertThat(playerByRandom.getBirthDate()).isEqualTo(randomPlayer.getBirthDate());
        assertThat(playerByRandom.getTeam()).isEqualTo(randomPlayer.getTeam());
    }

}