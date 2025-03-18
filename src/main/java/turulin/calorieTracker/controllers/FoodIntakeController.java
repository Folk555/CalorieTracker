package turulin.calorieTracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turulin.calorieTracker.dto.FoodIntake;
import turulin.calorieTracker.dto.Meal;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.repository.FoodIntakeRepository;
import turulin.calorieTracker.repository.MealRepository;
import turulin.calorieTracker.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/diet")
public class FoodIntakeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodIntakeRepository foodIntakeRepository;
    @Autowired
    private MealRepository mealRepository;

    @PostMapping("/add/{userId}?{mealId}")
    public ResponseEntity<FoodIntake> createDiet(@PathVariable Long userId, @PathVariable Long mealId) {
        Meal meal = mealRepository.findById(mealId).get();
        UserCalories user = userRepository.findById(userId).get();
        if (meal == null || user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        FoodIntake foodIntake = foodIntakeRepository.findById(mealId).get();
        if (foodIntake == null) {
            foodIntake = new FoodIntake();
            foodIntake.setMeals(new ArrayList<>());
        }
        foodIntake.setUser(user);
        foodIntake.getMeals().add(meal);
        foodIntake.setDate(LocalDate.now());
        FoodIntake save = foodIntakeRepository.save(foodIntake);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodIntake> getDietById(@PathVariable Long id) {
        Optional<FoodIntake> diet = foodIntakeRepository.findById(id);
        return diet.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable Long id) {
        if (foodIntakeRepository.existsById(id)) {
            foodIntakeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
