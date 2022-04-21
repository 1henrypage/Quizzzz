package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ActivityController {

    private final ActivityRepository repo;

    /**
     * Constructor for ActivityController
     * @param repo
     */
    public ActivityController(ActivityRepository repo){
        this.repo = repo;
    }

    /**
     * API endpoint for getting all the activities
     * @return null if empty, all the activities otherwise
     */
    @GetMapping(path="/activities") // "/api/admin/activities"
    public List<Activity> getAllActivities () {
        if (repo.count() == 0) {
            return new ArrayList<>();
        }
        return repo.findAll();
    }

    /**
     * API endpoint for adding/updating an activity
     * @param activity to put in the database
     * @return BadRequest if activity is null, the activity otherwise
     */
    @PostMapping(path="/addactivity") // "/api/admin/addactivity"
    public ResponseEntity<Activity> addActivity (@RequestBody Activity activity) {
        if (activity == null) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("[INFO] Adding activity, count: " + repo.count());
        repo.save(activity);
        System.out.println("[INFO] Added activity, count: " + repo.count());
        return ResponseEntity.ok(activity);
    }

    /**
     * API endpoint for deleting an activity
     * @param id of the activity to delete
     * @return BadRequest if activity doesn't exist, String otherwise
     */
    @GetMapping(path="/deleteactivity/{id}") // "/api/admin/deleteactivity/{id}"
    public ResponseEntity<String> deleteActivity (@PathVariable("id") String id) {
        if (repo.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("[INFO] Deleting activity, count: " + repo.count());
        repo.deleteById(id);
        System.out.println("[INFO] Deleted activity, count: " + repo.count());
        return ResponseEntity.ok("Activity " + id + " deleted");
    }

    /**
     * API endpoint for adding/updating an activity
     * @param path to give the image
     * @param image to put on the server
     * @return BadRequest if activity is null, the activity otherwise
     */
    @PostMapping(path="/uploadimage/{path}") // "/api/admin/addactivity"
    public ResponseEntity<Boolean> addActivity (@PathVariable("path") String path, @RequestBody byte[] image) {
        if (image == null) {
            return ResponseEntity.badRequest().build();
        }
        //all / in path were replaced by [+] to work with the url
        path = path.replace( "[+]", "/");
        System.out.println("[INFO] Adding image, path: " + path);
        InputStream inputStream = new ByteArrayInputStream(image);

        try {
            File file = new File("activitybank/" + path);
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(true);
    }
}
