package model;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    // Attributes
    private Long id;
    private String name;
    private int kcal;
    private List<MealIngredient> mealIngredients;
    private HealthTag healthTag;
    private byte[] imageData;

    // Constructors

    public Meal() {
        this.mealIngredients = new ArrayList<>();
    }


    public Meal(String name, int kcal, List<MealIngredient> mealIngredients, HealthTag healthTag) {
        this.name = name;
        this.kcal = kcal;
        this.mealIngredients = mealIngredients;
        this.healthTag = healthTag;
    }


    // Methods
    public void addIngredient(Ingredient ingredient, double amount, String unit) {
        // check if ingredint is already added error if so
        MealIngredient mealIngredient = new MealIngredient(ingredient, amount, unit);
        mealIngredients.add(mealIngredient);
    }


    public void removeIngredient(MealIngredient mealIngredient) {
        // check if ingredint was added even
        mealIngredients.remove(mealIngredient);
    }

    public void updateIngredient(MealIngredient mealIngredient, double amount, String unit) {
        // check if ingredint is already added error if so
        //MealIngredient oldMealIngredient = mealIngredients.get(mealIngredient);
        mealIngredient.setAmount(amount);
        mealIngredient.setUnit(unit);
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public List<MealIngredient> getMealIngredients() {
        return mealIngredients;
    }

    public void setMealIngredients(List<MealIngredient> mealIngredients) {
        this.mealIngredients = mealIngredients;
    }

    public HealthTag getHealthTag() {
        return healthTag;
    }

    public void setHealthTag(HealthTag healthTag) {
        this.healthTag = healthTag;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public String fullToString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kcal=" + kcal +
                ", mealIngredients=" + mealIngredients +
                ", healthTag=" + healthTag +
                '}';
    }
}
