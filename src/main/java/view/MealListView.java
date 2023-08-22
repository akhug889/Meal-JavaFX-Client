package view;

import controller.MealAppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Meal;
import rest.MealAppWebRepository;

import java.util.List;

public class MealListView extends Stage implements Observer {

    // GUI Elements
    private static final int SCENE_WIDTH = 400;
    private static final int SCENE_HEIGHT = 400;
    private final TableView<Meal> mealTableView;
    private Button createButton;

    // Controller
    private MealAppController controller;


    // Constructor
    public MealListView(MealAppController controller) {
        this.controller = controller;
        this.mealTableView = new TableView<>(FXCollections.observableArrayList(controller.getAllMeals()));

        // Set up the TableView
        TableColumn<Meal, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Meal, Integer> kcalColumn = new TableColumn<>("Kcal");
        kcalColumn.setCellValueFactory(new PropertyValueFactory<>("kcal"));

        TableColumn<Meal, String> healthTagColumn = new TableColumn<>("HealthTag");
        healthTagColumn.setCellValueFactory(new PropertyValueFactory<>("healthTag"));

        mealTableView.getColumns().add(nameColumn);
        mealTableView.getColumns().add(kcalColumn);
        mealTableView.getColumns().add(healthTagColumn);

        // Add this view as an observer for each Meal object and the MealRepository
        List<Meal> mealList = controller.getAllMeals();
        controller.addObserver(this);


        generateUserInterface();
        controller.setMealListView(this);
    }



    private void selectMeal(Meal meal) {
        controller.openSelectMealView(meal);
    }

    private void createMeal() {
        controller.openCreateMealView(new Meal());
    }

    @Override
    public void update() {
        //meals.clear();
        //meals.addAll(controller.getAllMeals());
        displayMeals();
    }

    private void displayMeals() {
        List<Meal> meals = controller.getAllMeals();  // Request data from the controller
        mealTableView.getItems().setAll(meals);
    }

    private void generateUserInterface() {
        VBox vbox = new VBox();

        createButton = new Button("Create a Meal");
        createButton.setOnAction(event -> createMeal());

        Label mealListLabel = new Label("Meal List");
        mealTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mealTableView.setOnMouseClicked(event -> selectMeal(mealTableView.getSelectionModel().getSelectedItem()));
        vbox.getChildren().addAll(mealListLabel, mealTableView, createButton);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
        setTitle("Meal List");
        update();
    }
}

