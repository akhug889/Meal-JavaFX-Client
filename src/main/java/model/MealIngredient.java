package model;

public class MealIngredient {

    // Attributes
    private Ingredient ingredient;
    private double amount;
    private String unit;

    // Constructor

    public MealIngredient() {
    }

    public MealIngredient(Ingredient ingredient, double amount, String unit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    // Getters and Setters
    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
