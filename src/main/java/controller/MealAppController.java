package controller;

import javafx.scene.control.Alert;
import model.*;

import rest.MealAppWebRepository;
import view.MealDetailView;
import view.MealListView;
import view.MealUpdateView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MealAppController extends Observable {

    // Attributes
    private MealListView mealListView;
    private MealDetailView mealDetailView;
    private MealUpdateView mealUpdateView;

    // Data Layer
    private final MealAppRepository mealAppRepository;


    // Constructor
    public MealAppController(MealAppRepository mealAppRepository) {
        this.mealAppRepository = mealAppRepository;
    }


    // CRUDE Operations - Meal
    public List<Meal> getAllMeals() {
        return mealAppRepository.getAllMeals();
    }

    public void createMeal(Meal meal) {
        mealAppRepository.createMeal(meal);
        notifyObservers();
    }

    public void updateMeal(Meal meal, Long id) {
        mealAppRepository.updateMeal(meal, id);
        notifyObservers();
    }

    public void deleteMeal(Long id) {
        mealAppRepository.deleteMeal(id);
        notifyObservers();
    }


    // Managing Views
    public void openSelectMealView(Meal meal) {
        mealUpdateView = new MealUpdateView(this, meal);
        mealUpdateView.show();
    }

    public void openCreateMealView(Meal meal) {
        mealDetailView = new MealDetailView(this, meal);
        mealDetailView.show();
    }

    // Getters and Setters
    public void setMealListView(MealListView mealListView) {
        this.mealListView = mealListView;
    }


}

