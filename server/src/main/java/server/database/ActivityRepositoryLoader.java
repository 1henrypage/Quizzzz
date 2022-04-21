package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class ActivityRepositoryLoader {
    private boolean LOAD_ACTIVITIES = false; // CHANGE THIS WHEN LOADING ACTIVITIES

    @Bean
    ApplicationRunner loadActivities(ActivityRepository repository) {
        return args -> {
            try {
                if(LOAD_ACTIVITIES) {
                    List<Activity> activities = (new ObjectMapper()).readValue(
                            new File("activitybank/activities.json"),
                            new TypeReference<>() {
                            });
                    activities = activities.stream().filter(Activity::isAppropriate).toList();

                    repository.saveAll(activities);

                    System.out.println("[INFO] Activity repository loaded with " + repository.count() + " entries.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
