package turulin.calorieTracker;

import org.junit.jupiter.api.Test;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.enums.DietGoal;
import turulin.calorieTracker.enums.Gender;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorTest {

    @Test
    void harrisBenedict() {
        UserCalories userCalories = new UserCalories();
        userCalories.setName("John");
        userCalories.setEmail("john@gmail.com");
        userCalories.setAge(26);
        userCalories.setWeight(70);
        userCalories.setHeight(180);
        userCalories.setGoal(DietGoal.GAIN);
        userCalories.setGender(Gender.MALE);
        double expectedCalories = 2040.0;

        double actualCalories = CalorieCalculator.harrisBenedict(userCalories);

        assertEquals(expectedCalories, actualCalories, 0.1);
    }
}