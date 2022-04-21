package server.api.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import server.database.LeaderboardRepository;
import server.services.SinglePlayerGameService;


import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@WebMvcTest
class SinglePlayerGameControllerTest {

    MvcResult mainResult;

    @MockBean
    SinglePlayerGameService singlePlayerGameService;

    @MockBean
    LeaderboardRepository leaderboardRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void init() throws Exception {
        mainResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/game/singleplayer/create/")
                        .content(asJsonString("Test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void mockMVCTest() {
        assertNotNull(mockMvc);
    }


    @Test
    void startSinglePlayer() throws Exception {
        assertEquals("0",mainResult.getResponse().getContentAsString());
        var result1 = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/game/singleplayer/create/")
                        .content(asJsonString("Test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getQuestions() throws Exception {
        MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/game/singleplayer/questions/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getNextQuestion() throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/game/singleplayer/getquestion/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void validateAnswer() throws Exception {
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/game/singleplayer/validate/0")
                .content(asJsonString(new Submission("1",7.7)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void deleteGame() throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/game/singleplayer/delete/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}