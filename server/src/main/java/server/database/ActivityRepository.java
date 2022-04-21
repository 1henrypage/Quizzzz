package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Random;

public interface ActivityRepository extends JpaRepository<Activity, String> {
    /**
     * Returns a random activity
     * @param random the random object
     * @return the activity
     *
     */
    default Activity getRandom(Random random){
        int count = (int) count();

        if(count <= 0) {
            return null;
        }

        return findAll().get(random.nextInt(count));
    }
}