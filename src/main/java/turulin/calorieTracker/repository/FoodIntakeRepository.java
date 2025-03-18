package turulin.calorieTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import turulin.calorieTracker.dto.FoodIntake;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodIntakeRepository extends JpaRepository<FoodIntake, Long> {

    @Query(value = "SELECT * FROM food_intake WHERE user_id = :userId AND date = :date", nativeQuery = true)
    List<FoodIntake> findByUserCaloriesIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM food_intake WHERE user_id = :userId", nativeQuery = true)
    List<FoodIntake> findByUserCaloriesId(@Param("userId") Long userId);
}
