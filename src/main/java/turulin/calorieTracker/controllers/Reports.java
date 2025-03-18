package turulin.calorieTracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import turulin.calorieTracker.dto.FoodIntake;
import turulin.calorieTracker.dto.Meal;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.repository.FoodIntakeRepository;
import turulin.calorieTracker.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class Reports {

    @Autowired
    private FoodIntakeRepository foodIntakeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/daily-eport/{userId}/{date}")
    public String getDailyReport(@PathVariable Long userId,
                                 @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<FoodIntake> dailyFood = foodIntakeRepository.findByUserCaloriesIdAndDate(userId, date);
        double totalCalories = dailyFood.stream().map(FoodIntake::getMeals)
                .flatMap(List::stream).mapToDouble(Meal::getCaloriesPerServing).sum();

        StringBuilder report = new StringBuilder();
        report.append("Отчет за ").append(date).append(":\n");
        report.append("Общее количество калорий: ").append(totalCalories).append("\n");
        report.append("Приемы пищи:\n");

        for (FoodIntake foodIntake : dailyFood) {
            for (Meal meal : foodIntake.getMeals()) {
                report.append("  Блюдо: ").append(meal.getName())
                        .append(", Калории: ").append(meal.getCaloriesPerServing())
                        .append(", Белки: ").append(meal.getProteins())
                        .append(", Жиры: ").append(meal.getFats())
                        .append(", Углеводы: ").append(meal.getCarbohydrates()).append("\n");
            }
        }
        return report.toString();
    }

    @GetMapping("/check-calories/{userId}/{date}")
    public String checkCalories(@PathVariable Long userId,
                                @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        UserCalories userCalories = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<FoodIntake> dailyFood = foodIntakeRepository.findByUserCaloriesIdAndDate(userId, date);
        double totalCalories = dailyFood.stream().map(FoodIntake::getMeals)
                .flatMap(List::stream).mapToDouble(Meal::getCaloriesPerServing).sum();

        if (totalCalories <= userCalories.getDailyCalories()) {
            return "Юзер " + userId + " уложился в норму калорий";
        } else {
            return "Юзер " + userId + " превысил в норму калорий";
        }
    }

    @GetMapping("/meal-history/{userId}")
    public String getMealHistory(@PathVariable Long userId) {
        List<FoodIntake> allFoodIntake = foodIntakeRepository.findByUserCaloriesId(userId);
        Map<LocalDate, List<FoodIntake>> entriesByDate = allFoodIntake.stream()
                .collect(Collectors.groupingBy(FoodIntake::getDate));
        List<LocalDate> dataList = new ArrayList<>(entriesByDate.keySet().stream().toList());
        dataList.sort(LocalDate::compareTo);

        StringBuilder report = new StringBuilder();
        for (LocalDate localDate : dataList) {
            report.append(localDate).append(" : \n");
            for (FoodIntake foodIntake : entriesByDate.get(localDate)) {
                for (Meal meal : foodIntake.getMeals()) {
                    report.append("  Блюдо: ").append(meal.getName())
                            .append(", Калории: ").append(meal.getCaloriesPerServing())
                            .append(", Белки: ").append(meal.getProteins())
                            .append(", Жиры: ").append(meal.getFats())
                            .append(", Углеводы: ").append(meal.getCarbohydrates()).append("\n");
                }
            }
        }

        return report.toString();
    }
}
