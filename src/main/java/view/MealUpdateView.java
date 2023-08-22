package view;

import controller.MealAppController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HealthTag;
import model.Ingredient;
import model.Meal;
import model.MealIngredient;

import java.util.Map;


public class MealUpdateView extends Stage implements Observer {

    // GUI Elements
    private static final int PADDING = 10;
    private static final int SCENE_HEIGHT = 300;
    private static final int SCENE_WIDTH = 600;
    private static final int GRID_VGAP = 8;
    private static final int GRID_HGAP = 10;
    private final TextField kcalTextField;
    private final TextField nameTextField;
    private final ComboBox<String> healthComboBox;
    private VBox ingredientVBox;
    private HBox lastRow = null;
    private Button saveButton;

    // Controller
    private MealAppController controller;
    private Meal meal;



    // Constructor
    public MealUpdateView(MealAppController controller, Meal meal) {
        this.meal = meal;
        this.kcalTextField = new TextField(Integer.toString(meal.getKcal()));
        this.nameTextField = new TextField(meal.getName());
        this.healthComboBox = new ComboBox<>();
        this.ingredientVBox = new VBox();

        // Populate the ingredient rows with existing ingredients if any if not simply show new empty row
        if (!meal.getMealIngredients().isEmpty()) {
            for (MealIngredient mealIngredient : meal.getMealIngredients()) {
                // Extract the information for all rows
                // TODO: Handle if not all aviable
                Ingredient ingredient = mealIngredient.getIngredient();
                double amount = mealIngredient.getAmount();
                String unit = mealIngredient.getUnit();

                // Add populated row
                addIngredientRow(ingredient, amount, unit);
            }
            // Add an empty row for new ingredient entry
            addIngredientRow();
        } else {
            addIngredientRow();
        }

        healthComboBox.getItems().addAll("HEALTHY", "NEUTRAL", "UNHEALTHY");
        healthComboBox.setValue(meal.getHealthTag().toString());
        this.controller = controller;

        // Add this view as an observer for the Course object
        controller.addObserver(this);

        generateUserInterface();

        // Add a listener to the window's close request event
        setOnCloseRequest(event -> controller.removeObserver(this));
    }


    // Methods
    private void save() {
        meal.setName(nameTextField.getText());
        meal.setKcal(Integer.parseInt(kcalTextField.getText()));
        HealthTag healthTag = HealthTag.valueOf(healthComboBox.getValue());
        meal.setHealthTag(healthTag);

        meal.getMealIngredients().clear();

        for (Node node : ingredientVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                String stringIngredient = ((TextField) row.getChildren().get(0)).getText();
                String stringAmount = ((TextField) row.getChildren().get(1)).getText();
                String unit = ((TextField) row.getChildren().get(2)).getText();

                // Check if all fields are filled
                if (!stringIngredient.trim().isEmpty() && !stringAmount.trim().isEmpty() && !unit.trim().isEmpty()) {
                    double amount = Double.parseDouble(stringAmount);
                    Ingredient ingredient = new Ingredient(stringIngredient);
                    meal.addIngredient(ingredient, amount, unit);
                }
            }
        }


        System.out.println(meal.fullToString());
        controller.updateMeal(meal, meal.getId());
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void update() {
        nameTextField.setText(meal.getName());
        kcalTextField.setText(Integer.toString(meal.getKcal()));



        setTitle(meal.getName());
    }

    private void generateUserInterface() {
        VBox vbox = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PADDING));
        grid.setVgap(GRID_VGAP);
        grid.setHgap(GRID_HGAP);



        Label nameLabel = new Label("Name ");
        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameTextField, 1, 0);

        Label kcalLabel = new Label("kcal: ");
        GridPane.setConstraints(kcalLabel, 0, 1);
        GridPane.setConstraints(kcalTextField, 1, 1);

        Label healthLabel = new Label("Health: ");
        GridPane.setConstraints(healthLabel, 0, 2);  // change the row to 2
        GridPane.setConstraints(healthComboBox, 1, 2); // change the row to 2

        Label ingredientLabel = new Label("Ingredients: ");
        GridPane.setConstraints(ingredientLabel, 0, 3);
        GridPane.setConstraints(ingredientVBox, 1, 3);

        saveButton = new Button("update meal");
        GridPane.setConstraints(saveButton, 0, 4);
        saveButton.setOnAction(event -> save());

        grid.getChildren().addAll(kcalLabel, nameLabel);
        grid.getChildren().addAll(kcalTextField, nameTextField);
        grid.getChildren().addAll(healthLabel, healthComboBox);  // add healthLabel and healthComboBox to grid
        grid.getChildren().addAll(ingredientLabel, ingredientVBox);
        grid.getChildren().add(saveButton);
        vbox.getChildren().add(grid);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
        if (this.meal.getName() != null) {
            setTitle(this.meal.getName());
        } else {
            setTitle("");
        }
    }

    private void addIngredientRow() {
        if (lastRow != null) {
            TextField lastUnitField = (TextField) lastRow.getChildren().get(2);
            if (lastUnitField.getText().trim().isEmpty()) {
                // Don't add a new row if the last unit field is empty
                return;
            }
        }

        TextField ingredientField = new TextField();
        ingredientField.setPromptText("Ingredient");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        TextField unitField = new TextField();
        unitField.setPromptText("Unit");

        HBox ingredientRow = new HBox();
        ingredientRow.setSpacing(10); // Add some spacing between the fields
        ingredientRow.getChildren().addAll(ingredientField, amountField, unitField);

        // Add listeners to the text fields to check for empty rows
        ChangeListener<String> textListener = (observable, oldValue, newValue) -> {
            if (ingredientField.getText().trim().isEmpty()
                    && amountField.getText().trim().isEmpty()
                    && unitField.getText().trim().isEmpty()) {
                // If all fields are empty, remove the row
                ingredientVBox.getChildren().remove(ingredientRow);

                // After deleting the row, request focus for the last unit field
                if (!ingredientVBox.getChildren().isEmpty()) {
                    HBox lastRow = (HBox) ingredientVBox.getChildren().get(ingredientVBox.getChildren().size() - 1);
                    TextField lastUnitField = (TextField) lastRow.getChildren().get(2);
                    Platform.runLater(lastUnitField::requestFocus);
                }

            } else if (!unitField.getText().trim().isEmpty()) {
                // If the unit field is filled, add a new row
                addIngredientRow();
            }
        };

        ingredientField.textProperty().addListener(textListener);
        amountField.textProperty().addListener(textListener);
        unitField.textProperty().addListener(textListener);

        lastRow = ingredientRow; // Keep track of the last row

        ingredientVBox.getChildren().add(ingredientRow);
    }

    private void addIngredientRow(Ingredient ingredient, double amount, String unit) {
        if (lastRow != null) {
            TextField lastUnitField = (TextField) lastRow.getChildren().get(2);
            if (lastUnitField.getText().trim().isEmpty()) {
                // Don't add a new row if the last unit field is empty
                return;
            }
        }

        TextField ingredientField = new TextField(ingredient.getName());
        ingredientField.setPromptText("Ingredient");
        TextField amountField = new TextField(Double.toString(amount));
        amountField.setPromptText("Amount");
        TextField unitField = new TextField(unit);
        unitField.setPromptText("Unit");

        HBox ingredientRow = new HBox();
        ingredientRow.setSpacing(10); // Add some spacing between the fields
        ingredientRow.getChildren().addAll(ingredientField, amountField, unitField);

        // Add listeners to the text fields to check for empty rows
        ChangeListener<String> textListener = (observable, oldValue, newValue) -> {
            if (ingredientField.getText().trim().isEmpty()
                    && amountField.getText().trim().isEmpty()
                    && unitField.getText().trim().isEmpty()) {
                // If all fields are empty, remove the row
                ingredientVBox.getChildren().remove(ingredientRow);

                // After deleting the row, request focus for the last unit field
                if (!ingredientVBox.getChildren().isEmpty()) {
                    HBox lastRow = (HBox) ingredientVBox.getChildren().get(ingredientVBox.getChildren().size() - 1);
                    TextField lastUnitField = (TextField) lastRow.getChildren().get(2);
                    Platform.runLater(lastUnitField::requestFocus);
                }

            } else if (!unitField.getText().trim().isEmpty()) {
                // If the unit field is filled, add a new row
                addIngredientRow();
            }
        };

        ingredientField.textProperty().addListener(textListener);
        amountField.textProperty().addListener(textListener);
        unitField.textProperty().addListener(textListener);

        lastRow = ingredientRow; // Keep track of the last row

        ingredientVBox.getChildren().add(ingredientRow);
    }

}

