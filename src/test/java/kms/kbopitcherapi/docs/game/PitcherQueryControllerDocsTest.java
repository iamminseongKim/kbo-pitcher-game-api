package kms.kbopitcherapi.docs.game;

import kms.kbopitcherapi.api.controller.game.PitcherQueryController;
import kms.kbopitcherapi.api.controller.game.dto.request.QuizRequest;
import kms.kbopitcherapi.api.service.PitcherQueryService;
import kms.kbopitcherapi.api.service.request.UsersPickPlayerServiceRequest;
import kms.kbopitcherapi.api.service.response.GameStatus;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.api.service.response.QuizResponse;
import kms.kbopitcherapi.docs.RestDocsSupport;
import kms.kbopitcherapi.domain.file.PlayerFile;
import kms.kbopitcherapi.domain.player.Player;
import kms.kbopitcherapi.domain.player.Position;
import kms.kbopitcherapi.domain.player.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PitcherQueryControllerDocsTest extends RestDocsSupport {

    private final PitcherQueryService pitcherQueryService = Mockito.mock(PitcherQueryService.class);
    private PlayerFile playerFile;
    private Player player;
    private PitcherResponse response;

    @Override
    protected Object initController() {
        return new PitcherQueryController(pitcherQueryService);
    }

    @BeforeEach
    void setUp() {
        playerFile = PlayerFile.builder()
                .originalFilename("origin.mp4")
                .quizFilename("quiz.mp4")
                .build();
        player = Player.builder()
                .name("김민성")
                .position(Position.SP)
                .birthDate(LocalDate.of(1997, 1, 1))
                .playerFile(playerFile)
                .team(Team.SK)
                .backNumber(29)
                .build();

        response = PitcherResponse.builder()
                .id(1L)
                .name(player.getName())
                .position(player.getPosition())
                .backNumber(player.getBackNumber())
                .birthDate(player.getBirthDate())
                .answerFileName(playerFile.getOriginalFilename())
                .quizFileName(playerFile.getQuizFilename())
                .team(player.getTeam())
                .build();
    }

    @Test
    @DisplayName("랜덤 선수를 조회하는 API")
    void getRandomPlayer() throws Exception {

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
                                fieldWithPath("data.position").type(JsonFieldType.STRING).description("랜덤 선수 투수 유형"),
                                fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("랜덤 선수 나이"),
                                fieldWithPath("data.backNumber").type(JsonFieldType.NUMBER).description("랜덤 선수 등번호"),
                                fieldWithPath("data.quizFileName").type(JsonFieldType.STRING).description("랜덤 선수 퀴즈 영상 url"),
                                fieldWithPath("data.answerFileName").type(JsonFieldType.STRING).description("랜덤 선수 정답 영상 url"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("자동완성 기능을 위해 Like 검색을 하고 조회를 한다.")
    void autoPitcher() throws Exception {
        PlayerFile playerFile2 = PlayerFile.builder()
                .originalFilename("player2.mp4")
                .quizFilename("quiz2.mp4")
                .build();
        Player player2 = Player.builder()
                .birthDate(LocalDate.of(1997, 1, 1))
                .position(Position.SP)
                .team(Team.SK)
                .backNumber(22)
                .name("김기덕")
                .playerFile(playerFile2)
                .build();

        PitcherResponse response2 = PitcherResponse.builder()
                .id(2L)
                .name(player2.getName())
                .position(player2.getPosition())
                .backNumber(player2.getBackNumber())
                .birthDate(player2.getBirthDate())
                .answerFileName(playerFile2.getOriginalFilename())
                .quizFileName(playerFile2.getQuizFilename())
                .team(player2.getTeam())
                .build();

        given(pitcherQueryService.autoPitcher(anyString()))
                .willReturn(List.of(response, response2));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/pitcher/auto/{name}", "김")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("pitcher-auto",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("name").description("자동 완성할 선수의 이름 또는 이름의 일부")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("자동 완성 선수 목록"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("선수의 고유 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("선수 이름"),
                                fieldWithPath("data[].team").type(JsonFieldType.STRING).description("선수가 속한 팀"),
                                fieldWithPath("data[].position").type(JsonFieldType.STRING).description("선수의 투수 유형"),
                                fieldWithPath("data[].age").type(JsonFieldType.NUMBER).description("선수 나이"),
                                fieldWithPath("data[].backNumber").type(JsonFieldType.NUMBER).description("선수 등번호"),
                                fieldWithPath("data[].quizFileName").type(JsonFieldType.STRING).description("퀴즈 영상 파일 이름"),
                                fieldWithPath("data[].answerFileName").type(JsonFieldType.STRING).description("정답 영상 파일 이름"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 응답 메시지")
                        )
                ));

    }

    @Test
    @DisplayName("랜덤선수를 맞춘다.")
    void submitQuiz() throws Exception {

        PlayerFile playerFile2 = PlayerFile.builder()
                .originalFilename("player2.mp4")
                .quizFilename("quiz2.mp4")
                .build();
        Player player2 = Player.builder()
                .birthDate(LocalDate.of(1997, 1, 1))
                .position(Position.SP)
                .team(Team.SK)
                .backNumber(22)
                .name("김기덕")
                .playerFile(playerFile2)
                .build();

        PitcherResponse response2 = PitcherResponse.builder()
                .id(2L)
                .name(player2.getName())
                .position(player2.getPosition())
                .backNumber(player2.getBackNumber())
                .birthDate(player2.getBirthDate())
                .answerFileName(playerFile2.getOriginalFilename())
                .quizFileName(playerFile2.getQuizFilename())
                .team(player2.getTeam())
                .build();

        QuizResponse quizResponse = QuizResponse.builder()
                .tryCount(2)
                .usersPickPitcherId(2L)
                .randomPitcherId(1L)
                .teamDiff(true)
                .positionDiff(true)
                .ageDiff(false)
                .gameStatus(GameStatus.WRONG)
                .backNumDiff(false)
                .randomPitcherResponse(response)
                .userPitcherResponse(response2)
                .build();

        given(pitcherQueryService.matchRandomPlayerBy(any(UsersPickPlayerServiceRequest.class), any(LocalDate.class)))
                .willReturn(quizResponse);

        QuizRequest request = QuizRequest.builder()
                .randomPlayerId(1L)
                .userPlayerId(2L)
                .tryCount(2).build();

        mockMvc.perform(post("/api/v1/pitcher/submit")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("pitcher-submit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("randomPlayerId").type(JsonFieldType.NUMBER).description("랜덤 선수 ID"),
                                fieldWithPath("userPlayerId").type(JsonFieldType.NUMBER).description("사용자 선택 선수 ID"),
                                fieldWithPath("tryCount").type(JsonFieldType.NUMBER).description("사용자 도전 횟수 (최대 6)")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("퀴즈 정보"),
                                fieldWithPath("data.usersPickPlayerServiceRequest").type(JsonFieldType.OBJECT).optional().description("사용자 선택 선수 요청 정보"),
                                fieldWithPath("data.usersPickPlayerServiceRequest.userPickPlayerId").type(JsonFieldType.NUMBER).description("사용자 선택 선수 ID"),
                                fieldWithPath("data.usersPickPlayerServiceRequest.randomPlayerId").type(JsonFieldType.NUMBER).description("랜덤 선수 ID"),
                                fieldWithPath("data.usersPickPlayerServiceRequest.tryCount").type(JsonFieldType.NUMBER).description("사용자 도전 횟수"),
                                fieldWithPath("data.usersPickPitcherId").type(JsonFieldType.NUMBER).optional().description("사용자 선택 선수 ID (오답 시에만 값 있음)"),
                                fieldWithPath("data.randomPitcherId").type(JsonFieldType.NUMBER).optional().description("랜덤 선수 ID (오답 시에만 값 있음)"),
                                fieldWithPath("data.gameStatus").type(JsonFieldType.STRING).description("게임 상태, 자세한 값은 gameStatus 표 참조"),
                                fieldWithPath("data.teamDiff").type(JsonFieldType.BOOLEAN).description("선택선수 랜덤선수 팀 비교 값"),
                                fieldWithPath("data.positionDiff").type(JsonFieldType.BOOLEAN).description("선택선수 랜덤선수 포지션 비교 값"),
                                fieldWithPath("data.ageDiff").type(JsonFieldType.BOOLEAN).description("선택선수 랜덤선수 나이 비교 값"),
                                fieldWithPath("data.backNumDiff").type(JsonFieldType.BOOLEAN).description("선택선수 랜덤선수 등번호 비교 값"),
                                fieldWithPath("data.tryCount").type(JsonFieldType.NUMBER).description("사용자 도전 횟수"),
                                fieldWithPath("data.randomPitcherResponse").type(JsonFieldType.OBJECT).description("랜덤 선수 정보"),
                                fieldWithPath("data.randomPitcherResponse.id").type(JsonFieldType.NUMBER).description("랜덤 선수 ID"),
                                fieldWithPath("data.randomPitcherResponse.name").type(JsonFieldType.STRING).description("랜덤 선수 이름"),
                                fieldWithPath("data.randomPitcherResponse.team").type(JsonFieldType.STRING).description("랜덤 선수 팀 이름"),
                                fieldWithPath("data.randomPitcherResponse.position").type(JsonFieldType.STRING).description("랜덤 선수 투수 유형"),
                                fieldWithPath("data.randomPitcherResponse.age").type(JsonFieldType.NUMBER).description("랜덤 선수 나이"),
                                fieldWithPath("data.randomPitcherResponse.backNumber").type(JsonFieldType.NUMBER).description("랜덤 선수 등번호"),
                                fieldWithPath("data.randomPitcherResponse.quizFileName").type(JsonFieldType.STRING).description("랜덤 선수 퀴즈 파일 정보"),
                                fieldWithPath("data.randomPitcherResponse.answerFileName").type(JsonFieldType.STRING).description("랜덤 선수 정답 파일 정보"),
                                fieldWithPath("data.userPitcherResponse").type(JsonFieldType.OBJECT).optional().description("사용자 선택 선수 정보 (오답 시에만)"),
                                fieldWithPath("data.userPitcherResponse.id").type(JsonFieldType.NUMBER).description("사용자 선택 선수 ID"),
                                fieldWithPath("data.userPitcherResponse.name").type(JsonFieldType.STRING).description("사용자 선택 선수 이름"),
                                fieldWithPath("data.userPitcherResponse.team").type(JsonFieldType.STRING).description("사용자 선택 선수 팀 이름"),
                                fieldWithPath("data.userPitcherResponse.position").type(JsonFieldType.STRING).description("사용자 선택 선수 투수 유형"),
                                fieldWithPath("data.userPitcherResponse.age").type(JsonFieldType.NUMBER).description("사용자 선택 선수 나이"),
                                fieldWithPath("data.userPitcherResponse.backNumber").type(JsonFieldType.NUMBER).description("사용자 선택 선수 등번호"),
                                fieldWithPath("data.userPitcherResponse.quizFileName").type(JsonFieldType.STRING).description("사용자 선택 선수 퀴즈 파일 정보"),
                                fieldWithPath("data.userPitcherResponse.answerFileName").type(JsonFieldType.STRING).description("사용자 선택 선수 정답 파일 정보"),
                                fieldWithPath("httpStatus").type(JsonFieldType.STRING).description("HTTP 응답 메시지")
                        )
                ));

    }


}
