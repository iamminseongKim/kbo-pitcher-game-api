package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.exception.GameException;
import kms.kbopitcherapi.api.exception.NoSearchAutoCompleteException;
import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import kms.kbopitcherapi.api.service.response.GameStatus;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.api.service.response.QuizResponse;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PitcherQueryService {

    private final PlayerRepository playerRepository;

    public PitcherResponse getRandomPlayer() {
        return PitcherResponse.ofRandom(playerRepository.findPlayerByRandom()
                .orElseThrow(() -> new GameException("선수가 1명도 없습니다.")));
    }

    public List<PitcherResponse> autoPitcher(String name) {
        List<Player> playerList = playerRepository.findByNameContaining(name);

        if (playerList.isEmpty()) {
            throw new NoSearchAutoCompleteException("선수 없음");
        }

        return playerList.stream()
                .map(PitcherResponse::of)
                .toList();
    }

    public QuizResponse matchRandomPlayerBy(UsersPickPlayerServiceRequest usersPickInfo, LocalDate today) {

        if (usersPickInfo.hasUserTryCountOver()) {
            // 사용자가 기회를 모두 소진
            Player randomPlayer = getRandomPlayerFrom(usersPickInfo);
            return QuizResponse.builder()
                    .gameStatus(GameStatus.GAME_OVER)
                    .randomPitcherResponse(PitcherResponse.of(randomPlayer))
                    .build();
        }

        if (usersPickInfo.userPickIsCorrect() && usersPickInfo.hasUserTryCount()) {
            // 사용자 정답이 맞음!
            Player randomPlayer = getRandomPlayerFrom(usersPickInfo);
            return QuizResponse.builder()
                    .gameStatus(GameStatus.CORRECT)
                    .randomPitcherResponse(PitcherResponse.of(randomPlayer))
                    .build();
        }

        // 랜덤선수의 값과, 유저 선택 값의 차이를 전달
        return getPlayerDiff(usersPickInfo, today);
    }

    private QuizResponse getPlayerDiff(UsersPickPlayerServiceRequest usersPickInfo, LocalDate today) {
        Player randomPlayer = getRandomPlayerFrom(usersPickInfo);
        Player userPickPlayer = getUserPickPlayerFrom(usersPickInfo);

        return QuizResponse.builder()
                .gameStatus(GameStatus.WRONG)
                .randomPitcherResponse(PitcherResponse.of(randomPlayer))
                .userPitcherResponse(PitcherResponse.of(userPickPlayer))
                .backNumDiff(randomPlayer.doseBackNumMatch(userPickPlayer.getBackNumber()))
                .positionDiff(randomPlayer.dosePositionMatch(userPickPlayer.getPosition()))
                .teamDiff(randomPlayer.doseTeamMatch(userPickPlayer.getTeam()))
                .ageDiff(randomPlayer.doseAgeMatch(userPickPlayer.getBirthDate(), today))
                .randomPitcherId(randomPlayer.getId())
                .usersPickPitcherId(userPickPlayer.getId())
                .tryCount(usersPickInfo.getTryCount())
                .build();
    }

    private Player getUserPickPlayerFrom(UsersPickPlayerServiceRequest usersPickInfo) {
        return playerRepository.findPlayerById(usersPickInfo.getUserPickPlayerId())
                .orElseThrow(() -> new GameException("선수가 없습니다.."));
    }

    private Player getRandomPlayerFrom(UsersPickPlayerServiceRequest usersPickInfo) {
        return playerRepository.findPlayerById(usersPickInfo.getRandomPlayerId())
                .orElseThrow(() -> new GameException("선수가 없습니다.."));
    }
}
