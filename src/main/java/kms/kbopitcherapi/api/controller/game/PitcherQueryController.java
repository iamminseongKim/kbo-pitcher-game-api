package kms.kbopitcherapi.api.controller.game;

import kms.kbopitcherapi.api.controller.ApiResponse;
import kms.kbopitcherapi.api.controller.game.dto.request.QuizRequest;
import kms.kbopitcherapi.api.service.PitcherQueryService;
import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.api.service.response.QuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PitcherQueryController {

    private final PitcherQueryService pitcherQueryService;

    @GetMapping("/api/v1/pitcher/random")
    public ApiResponse<PitcherResponse> getRandomPlayer() {
        return ApiResponse.ok(pitcherQueryService.getRandomPlayer());
    }

    @GetMapping("/api/v1/pitcher/auto/{name}")
    public ApiResponse<List<PitcherResponse>> autoPitcher(@PathVariable String name) {
        return ApiResponse.ok(pitcherQueryService.autoPitcher(name));
    }

    @PostMapping("/api/v1/pitcher/submit")
    public ApiResponse<QuizResponse> submitQuiz(@RequestBody QuizRequest request) {
        UsersPickPlayerServiceRequest usersPickPlayerServiceRequest = request.toUsersPickPlayerServiceRequest();
        return ApiResponse.ok(pitcherQueryService.matchRandomPlayerBy(usersPickPlayerServiceRequest, LocalDate.now()));
    }

}
