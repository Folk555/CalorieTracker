package turulin.calorieTracker.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turulin.calorieTracker.CalorieCalculator;
import turulin.calorieTracker.dto.Meal;
import turulin.calorieTracker.repository.MealRepository;

import java.util.Optional;

@RestController
@RequestMapping("/meal")
public class MealController {
    @Autowired
    private MealRepository mealRepository;

    @PostMapping("/add")
    public Meal createMeal(@Valid @RequestBody Meal meal) {
        meal.setId(null);
        return mealRepository.save(meal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable Long id, @RequestBody Meal updatedMeal) {
        return mealRepository.findById(id)
                .map(meal -> {
                    meal.setName(updatedMeal.getName());
                    meal.setCaloriesPerServing(updatedMeal.getCaloriesPerServing());
                    meal.setProteins(updatedMeal.getProteins());
                    meal.setFats(updatedMeal.getFats());
                    meal.setCarbohydrates(updatedMeal.getCarbohydrates());
                    Meal savedMeal = mealRepository.save(meal);
                    return new ResponseEntity<>(savedMeal, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        if (mealRepository.existsById(id)) {
            mealRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
