package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.api.service.response.QuizResponse;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PitcherQueryService {

    private final PlayerRepository playerRepository;

    public PitcherResponse getRandomPlayer() {
        return PitcherResponse.of(playerRepository.findPlayerByRandom());
    }

    public QuizResponse matchRandomPlayerBy(UsersPickPlayerServiceRequest usersPickPlayer) {

        if (usersPickPlayer.userPickIsCurrentAnswer()) {
            return null;
        }

        return null;
    }
}
