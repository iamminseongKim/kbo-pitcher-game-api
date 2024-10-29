package kms.kbopitcherapi.docs.game;

import kms.kbopitcherapi.api.controller.game.PitcherQueryController;
import kms.kbopitcherapi.api.service.PitcherQueryService;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.docs.RestDocsSupport;
import kms.kbopitcherapi.domain.file.PlayerFile;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PitcherQueryControllerDocsTest extends RestDocsSupport {

    private final PitcherQueryService pitcherQueryService = Mockito.mock(PitcherQueryService.class);

    @Override
    protected Object initController() {
        return new PitcherQueryController(pitcherQueryService);
    }

    @Test
    @DisplayName("랜덤 선수를 조회하는 API")
    void getRandomPlayer() throws Exception {
        PlayerFile playerFile = PlayerFile.builder()
                .originalFilename("origin.mp4")
                .quizFilename("quiz.mp4")
                .build();
        Player player = Player.builder()
                .name("김민성")
                .position(Position.SP)
                .birthDate(LocalDate.of(1997, 1, 1))
                .playerFile(playerFile)
                .team(Team.SK)
                .backNumber(29)
                .build();
        PitcherResponse response = PitcherResponse.builder()
                .id(1L)
                .name(player.getName())
                .position(player.getPosition())
                .backNumber(player.getBackNumber())
                .birthDate(player.getBirthDate())
                .answerFileName(playerFile.getOriginalFilename())
                .quizFileName(playerFile.getQuizFilename())
                .team(player.getTeam())
                .build();

        given(pitcherQueryService.getRandomPlayer())
                .willReturn(response);

        mockMvc.perform(get("/api/v1/pitcher/random"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("pitcher-getRandom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("랜덤 선수 정보"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("랜덤 선수 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("랜덤 선수 이름"),
                                fieldWithPath("data.team").type(JsonFieldType.STRING).description("랜덤 선수 팀명"),
                                fieldWithPath("data.position").type(JsonFieldType.STRING).description("랜덤 선수 포지션"),
                                fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("랜덤 선수 나이"),
                                fieldWithPath("data.backNumber").type(JsonFieldType.NUMBER).description("랜덤 선수 등번호"),
                                fieldWithPath("data.quizFileName").type(JsonFieldType.STRING).description("랜덤 선수 퀴즈 영상 url"),
                                fieldWithPath("data.answerFileName").type(JsonFieldType.STRING).description("랜덤 선수 정답 영상 url"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 응답 메시지")
                        )
                ));
    }

}
