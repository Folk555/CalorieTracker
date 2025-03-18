package turulin.calorieTracker.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import turulin.calorieTracker.enums.DietGoal;
import turulin.calorieTracker.enums.Gender;


@Entity
@Getter
@Setter
public class UserCalories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Min(value = 0) @Max(value = 130)
    @NotNull
    private Integer age;
    @Min(value = 0) @Max(value = 300)
    @NotNull
    private Integer weight;
    @Min(value = 30) @Max(value = 300)
    @NotNull
    private Integer height;
    @Enumerated(EnumType.STRING)
    @NotNull
    private DietGoal goal;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;
    private Double dailyCalories;
}

