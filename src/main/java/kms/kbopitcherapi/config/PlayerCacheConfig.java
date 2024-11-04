package kms.kbopitcherapi.config;

import kms.kbopitcherapi.api.service.PlayerCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PlayerCacheConfig {

    private final PlayerCacheService playerCacheService;

    public PlayerCacheConfig(PlayerCacheService playerCacheService) {
        this.playerCacheService = playerCacheService;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            playerCacheService.loadPlayerDataToRedis();
            log.info("======== 선수 캐싱 완료 ========");
        };
    }
}
