package kms.kbopitcherapi.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlayerCacheService {

    private final PlayerRepository playerRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String PLAYER_SEARCH = "player:search:";
    private static final String PLAYER_DATA = "player:data:";
    private static final double PREFIX_SCORE = 0.0;
    private static final double CONTAIN_SCORE = 1.0;


    public void loadPlayerDataToRedis() {
        List<Player> players = playerRepository.findAllForCache();

        for (Player player : players) {
            try {
                String playerKey = PLAYER_DATA + player.getId();
                String playerJson = objectMapper.writeValueAsString(player);
                redisTemplate.opsForValue().set(playerKey, playerJson);



                // 자동완성을 위한 prefix 저장
                String playerName = player.getName();
                for (int i = 0; i <= playerName.length(); i++) {
                    for (int j = i+1; j <= playerName.length(); j++) {
                        String searchKey = playerName.substring(i, j);
                        double score = (i == 0) ? PREFIX_SCORE : CONTAIN_SCORE;
                        redisTemplate.opsForZSet().add(PLAYER_SEARCH + searchKey, String.valueOf(player.getId()), score);
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("직렬화 실패 ", e);
            }
        }
    }

    public List<PitcherResponse> searchPlayersByPrefix(String searchText) {
        Set<String> playerIds = redisTemplate.opsForZSet()
                .range(PLAYER_SEARCH + searchText, 0, -1);

        if (CollectionUtils.isEmpty(playerIds)) {
            return Collections.emptyList();
        }

        return playerIds.stream()
                .map(id -> redisTemplate.opsForValue().get(PLAYER_DATA + id))
                .filter(Objects::nonNull)
                .map(playerJson -> {
                    try {
                        return objectMapper.readValue(playerJson, PitcherResponse.class);
                    } catch (JsonProcessingException e) {
                        log.error("역직렬화 실패");
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
