package rest;

import model.Meal;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

}