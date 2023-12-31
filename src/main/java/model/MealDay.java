package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealDay extends Observable {

    // Attributes
    private List<Meal> meals = new ArrayList<>();
    private LocalDate date;

    // Constructor

    public MealDay() {
    }

    public MealDay(LocalDate date) {
        this.date = date;
    }
    public MealDay(Meal meal, LocalDate date, int position) {
        meals.add(position, meal);
        this.date = date;
    }


    // Methods
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void asignMealAtPosition(Meal meal, int position) {
        meals.add(position, meal);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
    }

    // Getters and Setters

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
