package kms.kbopitcherapi.api.controller.game;

import kms.kbopitcherapi.api.controller.ApiResponse;
import kms.kbopitcherapi.api.service.PitcherQueryService;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PitcherQueryController {

    private final PitcherQueryService pitcherQueryService;

    @GetMapping("/api/v1/pitcher/random")
    public ApiResponse<PitcherResponse> getRandomPlayer() {
        return ApiResponse.ok(pitcherQueryService.getRandomPlayer());
    }

}
