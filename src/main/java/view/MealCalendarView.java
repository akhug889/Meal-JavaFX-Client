package view;

import controller.MealAppController;
import javafx.scene.Scene;
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
import java.util.List;

public class MealCalendarView extends Stage {

    private MealAppController controller;
    private DatePicker datePicker;
    private ListView<Meal> assignedMealsListView;
    private ListView<Meal> allMealsListView;

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
    }

    private void setupDragAndDrop() {
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

        assignedMealsListView.setOnDragOver(event -> {
            if (event.getGestureSource() != assignedMealsListView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        assignedMealsListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Meal mealToAdd = findMealByName(db.getString());
                if (mealToAdd != null) {
                    assignedMealsListView.getItems().add(mealToAdd);
                    allMealsListView.getItems().remove(mealToAdd);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private Meal findMealByName(String name) {
        for (Meal meal : allMealsListView.getItems()) {
            if (meal.getName().equals(name)) {
                return meal;
            }
        }
        return null;
    }
}