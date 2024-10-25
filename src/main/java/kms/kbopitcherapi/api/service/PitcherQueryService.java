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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PitcherQueryService {

    private final PlayerRepository playerRepository;

    public PitcherResponse getRandomPlayer() {
        return PitcherResponse.of(playerRepository.findPlayerByRandom()
                .orElseThrow(() -> new GameException("선수가 1명도 없습니다.")));
    }

    public QuizResponse matchRandomPlayerBy(UsersPickPlayerServiceRequest usersPickInfo) {

        if (usersPickInfo.hasUserTryCountOver() && usersPickInfo.userPickIsWrong()) {
            // 사용자가 기회를 모두 소진
            Player randomPlayer = getRandomPlayerFrom(usersPickInfo);
            return QuizResponse
                    .builder()
                    .gameStatus(GameStatus.GAME_OVER)
                    .randomPitcherResponse(PitcherResponse.of(randomPlayer))
                    .build();
        }

        if (usersPickInfo.userPickIsCorrect()) {
            // 사용자 정답이 맞음!
        }

        // 랜덤선수의 값과, 유저 선택 값의 차이를 전달
        return null;
    }

    private Player getRandomPlayerFrom(UsersPickPlayerServiceRequest usersPickInfo) {
        return playerRepository.findPlayerById(usersPickInfo.getRandomPlayerId())
                .orElseThrow(() -> new GameException("선수가 없습니다.."));
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
}
