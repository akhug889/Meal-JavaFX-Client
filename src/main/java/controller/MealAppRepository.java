package controller;

import model.Meal;
import model.MealDay;
import rest.MealAppWebRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MealAppRepository {

    // Attributes

    // In memory Data
    private List<Meal> meals;
    private List<MealDay> mealDays;

    private Boolean offlineMode = false;

    // Disk Data

    // Web Data
    private final MealAppWebRepository mealAppWebRepository;


    // Constructor
    public MealAppRepository(MealAppWebRepository mealAppWebRepository) {
        this.meals = new ArrayList<>();
        this.mealDays = new ArrayList<>();
        this.mealAppWebRepository = mealAppWebRepository;
    }

    // Methods


    // Getters and Setters
    public List<Meal> getAllMeals() {
        if (offlineMode) {
            //
        } else {
            meals = mealAppWebRepository.getAllMeals();
        }

        return new ArrayList<>(meals); // idk why new here??
    }


    public Meal getMealById(Long id) {
        return mealAppWebRepository.getMeal(id);
    }

    public Meal createMeal(Meal meal) {
        meals.add(meal);
        return mealAppWebRepository.createMeal(meal);
    }

    public void updateMeal(Meal meal, Long id) {
        mealAppWebRepository.updateMeal(meal, id);
    }

    public void deleteMeal(Long id) {
        mealAppWebRepository.deleteMeal(id);
    }



    // Getters and Setters for MealDays
    public List<MealDay> getAllMealDays() {
        if (offlineMode) {
            // Handle offline mode
        } else {
            mealDays = mealAppWebRepository.getAllMealDays();
        }
        return new ArrayList<>(mealDays);
    }

    public MealDay saveMealDay(MealDay mealDay) {
        if (!offlineMode) {
            return mealAppWebRepository.saveMealDay(mealDay);
        }
        // Handle offline mode if necessary
        return null;
    }


    public List<Meal> getAllMealsOfDay(Long mealDayId) {
        return mealAppWebRepository.getAllMealsOfDay(mealDayId);
    }

    public List<Meal> getAllMealsByDate(LocalDate date) {
        return mealAppWebRepository.getAllMealsByDate(date);
    }
}


