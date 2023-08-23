package view;

import controller.MealAppController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Meal;
import model.MealDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealCalendarView extends Stage implements Observer{

    private MealAppController controller;
    private DatePicker datePicker;
    private ListView<Meal> assignedMealsListView;
    private ListView<Meal> allMealsListView;
    private Button saveButton;
    private LocalDate selectedDate;


    public MealCalendarView(MealAppController controller) {
        this.controller = controller;

        datePicker = new DatePicker(LocalDate.now());
        assignedMealsListView = new ListView<>();
        allMealsListView = new ListView<>();

        setupDragAndDrop();

        BorderPane layout = new BorderPane();
        HBox listsLayout = new HBox(assignedMealsListView, allMealsListView);

        layout.setTop(datePicker);
        layout.setCenter(listsLayout);

        setScene(new Scene(layout, 500, 400));

        // Populate the allMealsListView with all available meals
        List<Meal> allMeals = controller.getAllMeals();
        allMealsListView.getItems().addAll(allMeals);

        this.selectedDate = LocalDate.now();
        // initaliy load load meals for today (if any)
        refreshAssignedMealsList();
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedDate = newValue;
            assignedMealsListView.getItems().clear();
            System.out.println(newValue);
            refreshAssignedMealsList();
        });


        saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveAssignedMeals());
        layout.setBottom(saveButton);

    }

    private void setupDragAndDrop() {
        // Drag detection for allMealsListView
        allMealsListView.setOnDragDetected(event -> {
            Meal selectedMeal = allMealsListView.getSelectionModel().getSelectedItem();
            if (selectedMeal != null) {
                Dragboard db = allMealsListView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedMeal.getName());
                db.setContent(content);
                event.consume();
            }
        });

        // Drag over event for allMealsListView
        allMealsListView.setOnDragOver(event -> {
            if (event.getGestureSource() != allMealsListView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Drop event for allMealsListView (removing meal from assignedMealsListView)
        allMealsListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Meal mealToRemove = findMealByName(db.getString());
                if (mealToRemove != null) {
                    assignedMealsListView.getItems().remove(mealToRemove);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Drag detection for assignedMealsListView
        assignedMealsListView.setOnDragDetected(event -> {
            Meal selectedMeal = assignedMealsListView.getSelectionModel().getSelectedItem();
            if (selectedMeal != null) {
                Dragboard db = assignedMealsListView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedMeal.getName());
                db.setContent(content);
                event.consume();
            }
        });

        // Drag over event for assignedMealsListView
        assignedMealsListView.setOnDragOver(event -> {
            if (event.getGestureSource() != assignedMealsListView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Drop event for assignedMealsListView (adding meal to assignedMealsListView)
        assignedMealsListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Meal mealToAdd = findMealByName(db.getString());
                if (mealToAdd != null) {
                    assignedMealsListView.getItems().add(mealToAdd);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }


    // Helper Methods --------------------------------------------------------------------------------------------------
    private Meal findMealByName(String name) {
        for (Meal meal : allMealsListView.getItems()) {
            if (meal.getName().equals(name)) {
                return meal;
            }
        }
        return null;
    }

    private void refreshAssignedMealsList() {
        List<Meal> mealsOfDay = controller.getAllMealsByDate(selectedDate);

        // Clear the current items and add the fetched meals
        assignedMealsListView.getItems().clear();
        assignedMealsListView.getItems().addAll(mealsOfDay);
    }



    private void saveAssignedMeals() {
        LocalDate selectedDate = datePicker.getValue();
        List<Meal> assignedMeals = new ArrayList<>(assignedMealsListView.getItems());

        MealDay mealDay = new MealDay(selectedDate);
        mealDay.setMeals(assignedMeals);

        controller.saveMealDay(mealDay);
    }

    @Override
    public void update() {
        // Populate the allMealsListView with all available meals
        List<Meal> allMeals = controller.getAllMeals();
        allMealsListView.getItems().addAll(allMeals);


    }
}
