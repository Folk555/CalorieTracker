package turulin.calorieTracker.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import turulin.calorieTracker.dto.UserCalories;
import turulin.calorieTracker.enums.DietGoal;
import turulin.calorieTracker.enums.Gender;
import turulin.calorieTracker.repository.UserRepository;

import java.util.Optional;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_userCreated() {
        UserCalories user = new UserCalories();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setAge(26);
        user.setWeight(70);
        user.setHeight(178);
        user.setGoal(DietGoal.LOSE);
        user.setGender(Gender.MALE);
        Mockito.when(userRepository.save(Mockito.any(UserCalories.class))).thenReturn(user);

        ResponseEntity<UserCalories> response = userController.createUser(user);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("John", response.getBody().getName());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserCalories.class));
    }
    @Test
    void getUserById_ReturnUser() {
        UserCalories user = new UserCalories();
        user.setId(1L);
        user.setName("John");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserCalories> response = userController.getUserById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("John", response.getBody().getName());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getUserById_UserNotExists_ReturnNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserCalories> response = userController.getUserById(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void updateUser_userUpdated() {
        UserCalories existingUser = new UserCalories();
        existingUser.setId(1L);
        existingUser.setName("John");
        UserCalories updatedUser = new UserCalories();
        updatedUser.setName("Franklin");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(UserCalories.class))).thenReturn(updatedUser);

        ResponseEntity<UserCalories> response = userController.updateUser(1L, updatedUser);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Franklin", response.getBody().getName());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserCalories.class));
    }

    @Test
    void updateUser_UserNotExists_ReturnsNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserCalories> response = userController.updateUser(1L, new UserCalories());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserCalories.class));
    }

    @Test
    void deleteUser_ReturnNoContent() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotExists_ReturnNotFound() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(userRepository, Mockito.never()).deleteById(1L);
    }
}