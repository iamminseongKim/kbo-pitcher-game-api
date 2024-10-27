package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.exception.GameException;
import kms.kbopitcherapi.api.exception.NoSearchAutoCompleteException;
import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import kms.kbopitcherapi.api.service.response.GameStatus;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.api.service.response.QuizResponse;
import kms.kbopitcherapi.domain.file.PlayerFile;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PitcherQueryServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PitcherQueryService pitcherQueryService;

    @Test
    @DisplayName("랜덤 선수를 한명 조회한다.")
    void getRandomPlayer() throws Exception {
        //given
        PlayerFile file = PlayerFile.builder()
                .quizFilename("test.mp4")
                .originalFilename("original.mp4")
                .build();
        Player randomPlayer = Player.builder()
                .name("김민성")
                .playerFile(file)
                .build();

        given(playerRepository.findPlayerByRandom())
                .willReturn(Optional.of(randomPlayer));

        //when
        PitcherResponse randomPitcherResponse = pitcherQueryService.getRandomPlayer();
        //then
        assertThat(randomPitcherResponse).isNotNull();
        assertThat(randomPitcherResponse.getName()).isEqualTo(randomPlayer.getName());
        assertThat(randomPitcherResponse.getQuizFileName()).isEqualTo(randomPlayer.getPlayerFile().getQuizFilename());

    }

    @Test
    @DisplayName("선수가 한명도 없을 때 문제를 요청하면 예외를 던진다.")
    void getRandomPlayerAtEmptyPlayer() throws Exception {
        //given
        given(playerRepository.findPlayerByRandom())
                .willReturn(Optional.empty());

        //when //then
        assertThatThrownBy(()-> pitcherQueryService.getRandomPlayer())
                .isInstanceOf(GameException.class)
                .hasMessage("선수가 1명도 없습니다.");
    }
    
    @Test
    @DisplayName("자동완성 기능을 위한 like 검색 결과 확인.")
    void autoPitcher() throws Exception {
        //given
        PlayerFile playerFile1 = makePlayerFile("original1", "quiz1");
        PlayerFile playerFile2 = makePlayerFile("original2", "quiz2");
        PlayerFile playerFile3 = makePlayerFile("original3", "quiz3");
        Player player1 = getPlayer(playerFile1, "김광현", 29, Team.SK, LocalDate.of(1988, 7, 22), Position.SP);
        Player player2 = getPlayer(playerFile2, "김택연", 63, Team.OB, LocalDate.of(2005,6, 3), Position.RP);
        Player player3 = getPlayer(playerFile3, "김서현", 54, Team.HH, LocalDate.of(2004,5, 31), Position.RP);
        
        given(playerRepository.findByNameContaining(anyString()))
                .willReturn(List.of(player1, player2, player3));
        //when
        List<PitcherResponse> autoPitcherList = pitcherQueryService.autoPitcher("김");
        //then
        assertThat(autoPitcherList).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder("김광현", "김택연", "김서현");
    }

    @Test
    @DisplayName("like 검색 결과 없으면 예외를 던진다.")
    void autoPitcherIsNull() throws Exception {
        //given
        given(playerRepository.findByNameContaining(anyString()))
                .willReturn(Collections.emptyList()); // null 넣으면 NPE 발생
        //when //then
        assertThatThrownBy(() -> pitcherQueryService.autoPitcher("김"))
                .isInstanceOf(NoSearchAutoCompleteException.class)
                .hasMessage("선수 없음");
    }

    @Test
    @DisplayName("게임 도전 수가 7이상이며, 해당 답이 틀렸을 경우 게임 오버 결과를 받는다.")
    void matchRandomPlayerGameOver() {
        // given
        UsersPickPlayerServiceRequest usersPick = UsersPickPlayerServiceRequest.builder()
                .tryCount(7)
                .randomPlayerId(1L)
                .userPickPlayerId(2L)
                .build();

        PlayerFile playerFile1 = makePlayerFile("original1", "quiz1");
        Player player1 = getPlayer(playerFile1, "김광현", 29, Team.SK, LocalDate.of(1988, 7, 22), Position.SP);

        given(playerRepository.findPlayerById(anyLong()))
                .willReturn(Optional.of(player1));

        // when
        QuizResponse quizResponse = pitcherQueryService.matchRandomPlayerBy(usersPick, null);
        // then
        assertThat(quizResponse.getGameStatus()).isEqualTo(GameStatus.GAME_OVER);
    }

    @Test
    @DisplayName("게임 도전 수가 6이하이며, 정답일 경우 정답 상태를 받는다")
    void matchRandomPlayerGameWin() {
        // given
        UsersPickPlayerServiceRequest usersPick = UsersPickPlayerServiceRequest.builder()
                .tryCount(6)
                .randomPlayerId(1L)
                .userPickPlayerId(1L)
                .build();

        PlayerFile playerFile1 = makePlayerFile("original1", "quiz1");
        Player player1 = getPlayer(playerFile1, "김광현", 29, Team.SK, LocalDate.of(1988, 7, 22), Position.SP);

        given(playerRepository.findPlayerById(anyLong()))
                .willReturn(Optional.of(player1));

        // when
        QuizResponse quizResponse = pitcherQueryService.matchRandomPlayerBy(usersPick, null);
        // then
        assertThat(quizResponse.getGameStatus()).isEqualTo(GameStatus.CORRECT);
    }

    @Test
    @DisplayName("게임 도전 수가 6이하이며, 오답일 경우 오답 상태와, 상대 정보를 받는다.")
    void test() {
        // given
        PlayerFile playerFile1 = makePlayerFile("original1", "quiz1");
        Player player1 = getPlayer(playerFile1, "김광현", 29, Team.SK, LocalDate.of(1988, 7, 22), Position.SP);

        PlayerFile playerFile2 = makePlayerFile("original2", "quiz2");
        Player player2 = getPlayer(playerFile2, "김택연", 63, Team.OB, LocalDate.of(1988,10, 28), Position.SP);

        LocalDate today = LocalDate.of(2024, 10, 27);

        given(playerRepository.findPlayerById(1L))
                .willReturn(Optional.of(player1));
        given(playerRepository.findPlayerById(2L))
                .willReturn(Optional.of(player2));

        UsersPickPlayerServiceRequest usersPick = UsersPickPlayerServiceRequest.builder()
                .tryCount(6)
                .randomPlayerId(1L)
                .userPickPlayerId(2L)
                .build();



        // when
        QuizResponse quizResponse = pitcherQueryService.matchRandomPlayerBy(usersPick, today);

        // then
        assertThat(quizResponse.getGameStatus()).isEqualTo(GameStatus.WRONG);
        assertThat(quizResponse.isBackNumDiff()).isFalse();
        assertThat(quizResponse.isTeamDiff()).isFalse();
        assertThat(quizResponse.isPositionDiff()).isTrue();
    }



    private Player getPlayer(PlayerFile playerFile, String name, int backNumber, Team team, LocalDate birthDate, Position position) {
        return Player.builder()
                .playerFile(playerFile)
                .name(name)
                .backNumber(backNumber)
                .team(team)
                .birthDate(birthDate)
                .position(position)
                .build();
    }

    private PlayerFile makePlayerFile(String originalFilename, String quizFilename) {
        return PlayerFile.builder()
                .originalFilename(originalFilename)
                .quizFilename(quizFilename)
                .build();
    }


}