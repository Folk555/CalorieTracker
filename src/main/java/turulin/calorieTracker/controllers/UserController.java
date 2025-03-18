package turulin.calorieTracker.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turulin.calorieTracker.CalorieCalculator;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<UserCalories> createUser(@Valid @RequestBody UserCalories userCalories) {
        userCalories.setDailyCalories(CalorieCalculator.harrisBenedict(userCalories));
        userCalories.setId(null);
        UserCalories save = userRepository.save(userCalories);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCalories> getUserById(@PathVariable Long id) {
        Optional<UserCalories> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCalories> updateUser(@PathVariable Long id, @RequestBody UserCalories updatedUserCalories) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUserCalories.getName());
                    user.setEmail(updatedUserCalories.getEmail());
                    user.setAge(updatedUserCalories.getAge());
                    user.setWeight(updatedUserCalories.getWeight());
                    user.setHeight(updatedUserCalories.getHeight());
                    user.setGoal(updatedUserCalories.getGoal());
                    user.setGender(updatedUserCalories.getGender());
                    UserCalories savedUserCalories = userRepository.save(user);
                    return new ResponseEntity<>(savedUserCalories, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
