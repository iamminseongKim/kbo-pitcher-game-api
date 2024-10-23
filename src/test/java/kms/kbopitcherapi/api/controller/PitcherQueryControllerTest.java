package kms.kbopitcherapi.api.controller;

import kms.kbopitcherapi.api.service.PitcherQueryService;
import kms.kbopitcherapi.api.service.response.PitcherResponse;
import kms.kbopitcherapi.domain.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PitcherQueryController.class)
class PitcherQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PitcherQueryService pitcherQueryService;

    @Test
    @DisplayName("랜덤 선수 한명을 조회한다.")
    void getRandomPitcher() throws Exception {
        //given
        Player player = Player.builder()
                .name("김민성")
                .build();
        PitcherResponse response = PitcherResponse.of(player);
        given(pitcherQueryService.getRandomPlayer())
                .willReturn(response);

        //when
        //then
        mockMvc.perform(get("/api/v1/pitcher/random"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(player.getName()));


    }

}