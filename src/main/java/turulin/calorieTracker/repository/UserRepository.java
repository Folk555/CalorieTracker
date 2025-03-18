package turulin.calorieTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turulin.calorieTracker.dto.UserCalories;

@Repository
public interface UserRepository extends JpaRepository<UserCalories, Long> {
}
