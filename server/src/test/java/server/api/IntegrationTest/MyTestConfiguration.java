package server.api.IntegrationTest;

import commons.Activity;
import commons.LeaderboardEntry;
import org.springframework.context.annotation.Bean;
import server.api.QuestionGenerator;
import server.database.ActivityRepository;
import server.database.LeaderboardRepository;
import server.database.MockActivityRepository;
import server.database.MockLeaderboardRepository;
import server.server_classes.IdGenerator;
import server.services.MultiPlayerGameService;
import server.services.SinglePlayerGameService;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@org.springframework.boot.test.context.TestConfiguration
public class MyTestConfiguration {

    /**
     * Used for mocking a singleplayer game service
     * @return The mocked singleplayer game service
     */
    @Bean
    public SinglePlayerGameService singlePlayerGameService() {
        Activity a1 = new Activity(
                "1","examplePath",
                "Activity1",23.4,
                "www.exam.com");

        Activity a2 = new Activity(
                "2","examplePath",
                "Activity2",92.5,
                "www.higher.com");

        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        MockActivityRepository mockRepo = new MockActivityRepository();
        mockRepo.saveAll(List.of(a1,a2,a3));
        QuestionGenerator generator = new QuestionGenerator(mockRepo,new Random(9999));

        return new SinglePlayerGameService(new IdGenerator(),new HashMap<>(),generator);
    }

    /**
     * Bean for Leaderboard Repo
     * @return A bean containing the mocked leaderboard
     */
    @Bean
    public LeaderboardRepository leaderboardRepository() {
        MockLeaderboardRepository repo = new MockLeaderboardRepository();
        LeaderboardEntry e1 = new LeaderboardEntry("Boris",560L);
        LeaderboardEntry e2 = new LeaderboardEntry("Simon",902L);
        LeaderboardEntry e3 = new LeaderboardEntry("Tyrone",2L);
        repo.saveAll(List.of(e1,e2,e3));
        return repo;
    }

    /**
     * Test Bean for Activity Repository
     *
     * @return "A mocked activity repo"
     */
    @Bean
    public ActivityRepository activityRepository() {
        Activity a1 = new Activity(
                "1","examplePath",
                "Activity1",23.4,
                "www.exam.com");

        Activity a2 = new Activity(
                "2","examplePath",
                "Activity2",92.5,
                "www.higher.com");

        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        MockActivityRepository mockRepo = new MockActivityRepository();
        mockRepo.saveAll(List.of(a1,a2,a3));
        return mockRepo;
    }

    /**
     * A configuration for multiplayer game service
     * @return A "mocked" multiplayergameservice
     */
    @Bean
    public MultiPlayerGameService multiPlayerGameService() {
        Activity a1 = new Activity(
                "1","examplePath",
                "Activity1",23.4,
                "www.exam.com");

        Activity a2 = new Activity(
                "2","examplePath",
                "Activity2",92.5,
                "www.higher.com");

        Activity a3 = new Activity(
                "3","examplePath",
                "Activity3",24.5,
                "www.need.com");
        MockActivityRepository repo = new MockActivityRepository();
        repo.saveAll(List.of(a1,a2,a3));
        return new MultiPlayerGameService
                (new IdGenerator(),new HashMap<>(),
                        new QuestionGenerator(repo,new Random(9999)));

    }


}
