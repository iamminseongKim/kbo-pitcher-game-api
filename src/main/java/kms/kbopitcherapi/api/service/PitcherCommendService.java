package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PitcherCommendService {

    private final PlayerRepository playerRepository;

    @Transactional
    public int insertPitchers(List<PlayerCommendServiceRequest> pitcherRequestList) {

        List<Player> playerList = pitcherRequestList.stream()
                .map(PlayerCommendServiceRequest::getPlayer)
                .toList();

        List<Player> savedPlayers = playerRepository.saveAll(playerList);

        return savedPlayers.size();
    }

}
