package turulin.calorieTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turulin.calorieTracker.dto.Meal;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
}
