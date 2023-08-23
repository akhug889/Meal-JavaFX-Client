package rest;

import model.Meal;
import model.MealDay;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MealAppWebRepository {

    // Attributes
    private static final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate rest;


    // Constructor
    public MealAppWebRepository() {
        rest = new RestTemplate();
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    // Methods
    public List<Meal> getAllMeals() {
        String url = BASE_URL + "/meals";
        ResponseEntity<List<Meal>> responseEntity = rest.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Meal>>() {}
        );

        return responseEntity.getBody();
    }

    // Get meal by id
    public Meal getMeal(Long id) {
        String url = BASE_URL + "/meals/" + id;
        return rest.getForObject(url, Meal.class);
    }

    // Create a new meal
    public Meal createMeal(Meal meal) {
        String url = BASE_URL + "/meals";
        return rest.postForObject(url, meal, Meal.class);
    }

    // Update an existing meal
    public void updateMeal(Meal meal, Long id) {
        String url = BASE_URL + "/meals/" + id;
        rest.put(url, meal);
    }

    // Delete a meal
    public void deleteMeal(Long id) {
        String url = BASE_URL + "/meals/" + id;
        rest.delete(url);
    }


    public MealDay saveMealDay(MealDay mealDay) {
        String url = BASE_URL + "/meal-days";
        ResponseEntity<MealDay> response = rest.exchange(url, HttpMethod.POST, new HttpEntity<>(mealDay), MealDay.class);
        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        // Handle other responses or errors as needed
        return null;
    }


    // idk if this needed
    public List<MealDay> getAllMealDays() {
        String url = BASE_URL + "/meal-days";
        ResponseEntity<List<MealDay>> response = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<MealDay>>() {});
        return response.getBody();
    }

    public List<Meal> getAllMealsOfDay(Long mealDayId) {
        String url = BASE_URL + "/meals-days/" + mealDayId;
        Meal[] mealsArray = rest.getForObject(url, Meal[].class);
        return Arrays.asList(mealsArray);
    }

    public List<Meal> getAllMealsByDate(LocalDate date) {
        String url = BASE_URL + "/meals-days/date/" + date;
        Meal[] mealsArray = rest.getForObject(url, Meal[].class);
        return Arrays.asList(mealsArray);
    }


}
