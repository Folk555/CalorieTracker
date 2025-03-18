package turulin.calorieTracker;

import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.enums.DietGoal;
import turulin.calorieTracker.enums.Gender;

public class CalorieCalculator {
    public static double harrisBenedict(UserCalories userCalories) {
        double bmr;
        if (userCalories.getGender().equals(Gender.MALE)) {
            bmr = (10 * userCalories.getWeight()) + (6.25 * userCalories.getHeight()) - (5 * userCalories.getAge()) + 5;
        } else {
            bmr = (10 * userCalories.getWeight()) + (6.25 * userCalories.getHeight()) - (5 * userCalories.getAge()) - 161;
        }

        switch (userCalories.getGoal()) {
            case DietGoal.LOSE:
                return bmr * 0.8;
            case DietGoal.KEEP:
                return bmr;
            case DietGoal.GAIN:
                return bmr * 1.2;
            default:
                return bmr;
        }
    }
}
