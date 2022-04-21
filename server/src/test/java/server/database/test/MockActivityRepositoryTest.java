package server.database.test;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.MockActivityRepository;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MockActivityRepositoryTest {

    private MockActivityRepository repo;
    private Activity a1, a2, a3, a4;

    /**
     * Sets up all the variables for the tests (even for upcoming tests may we decide to add some)
     */
    @BeforeEach
    public void setup() {
        repo = new MockActivityRepository();
        a1 = new Activity("act_01", "/act1.bmp", "Activity 1", 3.3, "facebook.com");
        a2 = new Activity("act_02", "/act2.bmp", "Activity 2", 50, "twitter.com");
        a3 = new Activity("act_03", "/act3.bmp", "Activity 3", 60, "google.com");
        a4 = new Activity("act_04", "/act4.bmp", "Activity 4", 80, "bing.com");
    }

    /**
     * Tests the save method of ActivityRepository
     */
    @Test
    public void testSave(){
        repo.save(a1);
        assertEquals("save", repo.calledMethods.get(repo.calledMethods.size()-1));
        assertEquals(a1, repo.activities.get(0));
    }

    /**
     * Tests the saveAll method of ActivityRepository
     */
    @Test
    public void testSaveAll(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        activities.add(a3);
        activities.add(a4);
        repo.saveAll(activities);
        assertEquals("save", repo.calledMethods.get(0));
        assertEquals("save", repo.calledMethods.get(1));
        assertEquals("save", repo.calledMethods.get(2));
        assertEquals("save", repo.calledMethods.get(3));
        assertEquals(a1, repo.activities.get(0));
        assertEquals(a2, repo.activities.get(1));
        assertEquals(a3, repo.activities.get(2));
        assertEquals(a4, repo.activities.get(3));
    }

    /**
     * Tests the delete method of ActivityRepository
     */
    @Test
    public void testDelete(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        repo.saveAll(activities);
        repo.delete(a1);
        assertEquals(1, repo.count());
        assertEquals(a2,repo.activities.get(0));
    }

    /**
     * Tests the deleteAll method of ActivityRepository
     */
    @Test
    public void testDeleteAll(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        activities.add(a3);
        activities.add(a4);
        repo.saveAll(activities);
        assertEquals(4,repo.count());
        repo.deleteAll();
        assertEquals(0, repo.count());
    }

    /**
     * Tests the existsById method of ActivityRepository
     */
    @Test
    public void testExistsByID(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        activities.add(a3);
        activities.add(a4);
        repo.saveAll(activities);
        assertTrue(repo.existsById("act_01"));
    }

    /**
     * Tests the existsById method of ActivityRepository in the case where the ID does not exist
     */
    @Test
    public void testDoesNotExistsByID(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        repo.saveAll(activities);
        assertFalse(repo.existsById("act_03"));
    }

    /**
     * Tests the deleteById method of ActivityRepository
     */
    @Test
    public void testDeleteByID(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        repo.saveAll(activities);
        repo.deleteById("act_02");
        assertEquals(a1, repo.activities.get(0));
    }

    /**
     * Tests the getById method of ActivityRepository
     */
    @Test
    public void testGetById(){
        List<Activity> activities = new ArrayList<>();
        activities.add(a1);
        activities.add(a2);
        activities.add(a3);
        activities.add(a4);
        repo.saveAll(activities);
        assertEquals(a3, repo.getById("act_03"));
    }
}