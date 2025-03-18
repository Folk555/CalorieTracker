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

    @PostMapping("/add")
    public ResponseEntity<FoodIntake> createDiet(@RequestParam Long userId,
                                                 @RequestParam Long mealId) {
        Optional<Meal> mealById = mealRepository.findById(mealId);
        Optional<UserCalories> userById = userRepository.findById(userId);
        if (mealById.isEmpty() || userById.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Optional<FoodIntake> foodIntakebyId = foodIntakeRepository.findById(mealId);
        FoodIntake foodIntake = null;
        if (foodIntakebyId.isEmpty()) {
            foodIntake = new FoodIntake();
            foodIntake.setMeals(new ArrayList<>());
        } else {
            foodIntake = foodIntakebyId.get();
        }
        foodIntake.setUser(userById.get());
        foodIntake.getMeals().add(mealById.get());
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
