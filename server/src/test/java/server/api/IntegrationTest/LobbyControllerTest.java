package server.api.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.PlayerData;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import server.api.MultiPlayerGameController;
import server.services.MultiPlayerGameService;




@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Import(MyTestConfiguration.class)
@WebMvcTest
class LobbyControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MultiPlayerGameController controller;

    @MockBean
    private MultiPlayerGameService service;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    //tests run in sequence, beans are not reset.

    @Test
    void startGame() throws Exception {
        var mainResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/lobby/connect")
                        .content(asJsonString(new PlayerData("H")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var secondResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/lobby/start")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void connect() throws Exception {
        var mainResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lobby/connect")
                .content(asJsonString(new PlayerData("H")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void disconnect() throws Exception {
        var mainResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/lobby/connect")
                        .content(asJsonString(new PlayerData("H")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();


        var secondResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/lobby/disconnect")
                        .content(asJsonString(new PlayerData("H")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void playersUpdater() throws Exception {
        var mainResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/lobby/connect")
                        .content(asJsonString(new PlayerData("H")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var secondResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/lobby/update")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
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