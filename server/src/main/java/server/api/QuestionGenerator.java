package server.api;

import java.util.*;

import commons.Activity;
import commons.Question;
import commons.QuestionType;
import org.springframework.stereotype.Component;
import server.database.ActivityRepository;

@Component
public class QuestionGenerator {
    private final ActivityRepository activityRepository;
    private final Random random;

    /**
     * constructor for QuestionGenerator
     * @param activityRepository the repository of activities
     * @param random a random object
     */
    public QuestionGenerator(ActivityRepository activityRepository, Random random){
        this.activityRepository = activityRepository;
        this.random = random;
    }

    /**
     * generates one question
     * @return the generated question
     */
    public Question generateQuestion(){
        int randomNumber = random.nextInt(4);
        if(randomNumber == 1) {
            return generateEstimateQuestion();
        }else if(randomNumber == 2) {
            return generateMCQuestion();
        }else{
            return generateSelectiveQuestion();
        }
    }

    private boolean addActivityToSetIfAppropriate(Set<Activity> result, List<Activity> activities) {
        var randomActivity = activities.get(random.nextInt(activities.size()));

        if (randomActivity == null) {
            return true;
        }
        var consumptions = result.stream().map(Activity::getEnergyConsumption).toList();
        var consumptionAlreadyInSet = consumptions.contains(randomActivity.getEnergyConsumption());
        var appropriate = Activity.isAppropriate(randomActivity);
        if (!consumptionAlreadyInSet && appropriate){
            result.add(Activity.createActivityWithImage(randomActivity));
        }
        return false;
    }

    private Set<Activity> generateActivitySet(QuestionType type){
        Set<Activity> result = new HashSet<>();
        Activity mainActivity;
        do {
            mainActivity = activityRepository.getRandom(random);
            if (mainActivity == null) {
                return null;
            }
        } while (!Activity.isAppropriate(mainActivity));

        result.add(Activity.createActivityWithImage(mainActivity));

        if(QuestionType.getAmountOfActivities(type) == 1){
            return result;
        }
        double minConsumption = mainActivity.getEnergyConsumption() * 0.6;
        double maxConsumption = Math.max(mainActivity.getEnergyConsumption() * 3, 300);
        List<Activity> activities = activityRepository.findAll().stream().filter(
                x -> x.getEnergyConsumption() > minConsumption && x.getEnergyConsumption() < maxConsumption).toList();

        if(activities.size() < QuestionType.getAmountOfActivities(type)){
            return generateActivitySet(type);
        }
        while(result.size() < QuestionType.getAmountOfActivities(type)){
            if (addActivityToSetIfAppropriate(result, activities)) {
                return null;
            }
        }
        return result;
    }

    private Question generateMCQuestion(){
        Set<Activity> activitySet = generateActivitySet(QuestionType.MC);
        if (activitySet == null) {
            return null;
        }
        String correctAnswer = "";
        String questionText;
        if(random.nextInt(2) == 0) {
            questionText = "Which one of the following consumes the least energy?";
            double minimumConsumption = Double.MAX_VALUE;
            for(var activity: activitySet){
                if(activity.getEnergyConsumption() < minimumConsumption){
                    correctAnswer = activity.getId();
                    minimumConsumption = activity.getEnergyConsumption();
                }
            }
        } else {
            questionText = "Which one of the following consumes the most energy?";
            double maximumConsumption = Double.MIN_VALUE;
            for(var activity: activitySet){
                if(activity.getEnergyConsumption() > maximumConsumption){
                    correctAnswer = activity.getId();
                    maximumConsumption = activity.getEnergyConsumption();
                }
            }
        }
        return new Question(questionText,activitySet,QuestionType.MC, correctAnswer);
    }

    private Question generateSelectiveQuestion(){
        Set<Activity> activitySet = generateActivitySet(QuestionType.SELECTIVE);
        if (activitySet == null) {
            return null;
        }
        String correctAnswer = "";
        String questionText;
        questionText = "How much energy does it take?";
        var activityIterator = activitySet.iterator();
        Activity activity1 = activityIterator.next();
        Activity activity2 = activityIterator.next();
        Activity activity3 = activityIterator.next();

        int randomActivityNumber = random.nextInt(3);
        switch (randomActivityNumber) {
            case 0 -> {
                correctAnswer = activity1.getId();
                questionText += " " + activity1.getTitle();
            }
            case 1 -> {
                correctAnswer = activity2.getId();
                questionText += " " + activity2.getTitle();
            }
            case 2 -> {
                correctAnswer = activity3.getId();
                questionText += " " + activity3.getTitle();
            }
        }
        return new Question(questionText,activitySet,QuestionType.SELECTIVE, correctAnswer);
    }

    private Question generateEstimateQuestion(){
        Set<Activity> activitySet = generateActivitySet(QuestionType.ESTIMATE);
        if (activitySet == null) {
            return null;
        }

        var activity = activitySet.iterator().next();
        String questionText = "How many watt-hours of energy does " + activity.getTitle() + " consume?";

        return new Question(questionText, activitySet, QuestionType.ESTIMATE,
                Double.toString(activity.getEnergyConsumption()));

    }


    /**
     * generates 20 questions
     * @return the generated questions in a list
     */
    public List<Question> generate20Questions(){
        List<Question> result = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            result.add(generateQuestion());
        }

        return result;
    }
}
