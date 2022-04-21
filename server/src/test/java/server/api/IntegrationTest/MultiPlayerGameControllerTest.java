package server.api.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.PlayerData;
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
import server.services.MultiPlayerGameService;

import java.util.List;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@WebMvcTest
class MultiPlayerGameControllerTest {

    MvcResult mainResult;

    @MockBean
    private MultiPlayerGameService multiPlayerGameService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() throws Exception {
        List<PlayerData> players = List.of(new PlayerData("Joe"),
                new PlayerData("Rack"));
        mainResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/game/multiplayer/create")
                .content(asJsonString(players))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test
    void getQuestions() throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/game/multiplayer/questions/0/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }

    @Test
    void getNextQuestion() throws Exception {
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/game/multiplayer/getquestion/0/Joe")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @Test
    void updater() throws Exception {
        MvcResult asyncListener = mockMvc.perform(MockMvcRequestBuilders
                .get("/update/0"))
                .andExpect(MockMvcResultMatchers.request().asyncNotStarted())
                .andReturn();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}