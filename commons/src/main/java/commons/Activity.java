package commons;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Entity
@Table(name = "activities")
public class Activity {
    public byte[] image;

    @Id
    @Column(name = "activity_id", nullable = false)
    @JsonProperty("id")
    public String id;

    @Column(name="image_path")
    @JsonProperty("image_path")
    private String imagePath;

    @Column(name = "title", nullable = false)
    @JsonProperty("title")
    private String title;

    @Column(name="energy_consumption", nullable = false)
    @JsonProperty("consumption_in_wh")
    private double energyConsumption;

    @Lob
    @Column(name="source_url")
    @JsonProperty("source")
    private String source;

    /**
     * constructor for object mapper
     */
    private Activity() {
        // for object mapper
    }

    /**
     * Constructor for Activity
     *
     * @param id the id of the activity
     * @param imagePath the path of the image
     * @param title the text of the activity
     * @param energyConsumption the energy consumption of the activity
     * @param source source of the information
     */
    public Activity(String id, String imagePath, String title, double energyConsumption, String source) {
        this.id = id;
        this.imagePath = imagePath;
        this.title = title;
        this.energyConsumption = energyConsumption;
        this.source = source;
    }

    /**
     * Creat an activity that has an image loaded into it
     * @param activity the activity to have the image loaded into
     * @return the activity that has the image loaded into it
     */
    public static Activity createActivityWithImage(Activity activity){
        Activity result = new Activity(activity.getId(), activity.getImagePath(), activity.getTitle(),
                activity.getEnergyConsumption(), activity.getSource());

        try {
            result.image = Files.readAllBytes(Path.of("activitybank/" + activity.imagePath));
        } catch (IOException e) {
            System.out.println("[ERROR] File does not exist: " + activity.imagePath);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Getter for id
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for imagePath
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Getter for imagePath
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Getter for title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for energyConsumption
     * @return The amount of energy the activity uses
     */
    public double getEnergyConsumption() {
        return energyConsumption;
    }

    /**
     * Getter for source
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Checks if the activity respects some criteria.
     * @param activity the activity to be checked
     * @return true iff the activity is appropriate for the game
     */
    public static boolean isAppropriate(Activity activity) {
        return  !activity.id.startsWith("60-") &&
                activity.energyConsumption > 0 &&
                activity.title.length() < 50 &&      // MAX_TEXT_LENGTH
                activity.energyConsumption < 1.0E8;  // DOUBLE
    }

    /**
     * enhanced equals method
     *
     * @param obj The object to be compared to
     * @return true if the two Objects have tested equals.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     *enhanced hashcode method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * enhanced toString
     *
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}