package kms.kbopitcherapi.api.service;

import kms.kbopitcherapi.api.service.request.PlayerCommendServiceRequest;
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
    public void insertPitchers(List<PlayerCommendServiceRequest> pitcherRequestList) {
        for (PlayerCommendServiceRequest playerRequest : pitcherRequestList) {
            log.info("신규 플레이어 저장, 이름 : {}, 팀 : {}", playerRequest.getName(), playerRequest.getTeam());
            playerRepository.save(playerRequest.getPlayer());
        }
    }

}
