package turulin.calorieTracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.enums.DietGoal;
import turulin.calorieTracker.enums.Gender;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_Success() throws Exception {
        UserCalories userCalories = new UserCalories();
        userCalories.setName("John");
        userCalories.setEmail("john@gmail.com");
        userCalories.setAge(26);
        userCalories.setWeight(70);
        userCalories.setHeight(180);
        userCalories.setGoal(DietGoal.GAIN);
        userCalories.setGender(Gender.MALE);
        String userJson = objectMapper.writeValueAsString(userCalories);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userCalories.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userCalories.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(userCalories.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight").value(userCalories.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value(userCalories.getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(userCalories.getGoal().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(userCalories.getGender().toString()));
    }

    @Test
    void createUser_incorrectData() throws Exception {
        UserCalories userCalories = new UserCalories();
        userCalories.setName("John");
        userCalories.setEmail("john@gmail.com");
        userCalories.setAge(999999);
        userCalories.setWeight(70);
        userCalories.setHeight(5);
        String userJson = objectMapper.writeValueAsString(userCalories);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value("must be less than or equal to 130"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value("must be greater than or equal to 30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value("must not be null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("must not be null"));
    }
}