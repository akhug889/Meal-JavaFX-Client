package view;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import controller.MealAppController;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.HealthTag;
import model.Ingredient;
import model.Meal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class MealDetailView extends Stage implements Observer {

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
    private Button selectImageButton;
    private ImageView selectedImageView;


    // Controller
    private MealAppController controller;
    private Meal meal;



    // Constructor
    public MealDetailView(MealAppController controller, Meal meal) {
        this.meal = meal;
        this.kcalTextField = new TextField(Integer.toString(meal.getKcal()));
        this.nameTextField = new TextField(meal.getName());
        this.healthComboBox = new ComboBox<>();
        this.ingredientVBox = new VBox();
        addIngredientRow();
        healthComboBox.getItems().addAll("HEALTHY", "NEUTRAL", "UNHEALTHY");
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
        controller.createMeal(meal);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void update() {
        kcalTextField.setText(Integer.toString(meal.getKcal()));
        nameTextField.setText(meal.getName());


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

        saveButton = new Button("create meal");
        GridPane.setConstraints(saveButton, 0, 4);
        saveButton.setOnAction(event -> save());

        grid.getChildren().addAll(kcalLabel, nameLabel);
        grid.getChildren().addAll(kcalTextField, nameTextField);
        grid.getChildren().addAll(healthLabel, healthComboBox);  // add healthLabel and healthComboBox to grid
        grid.getChildren().addAll(ingredientLabel, ingredientVBox);
        grid.getChildren().add(saveButton);
        vbox.getChildren().add(grid);

        selectImageButton = new Button("Select Image");
        selectedImageView = new ImageView();
        selectedImageView.setFitWidth(150); // Adjust as needed
        selectedImageView.setFitHeight(150); // Adjust as needed

        selectImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(this);
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                selectedImageView.setImage(image);
                byte[] imageData = imageToByteArray(image);
                meal.setImageData(imageData);

            }
        });

        GridPane.setConstraints(selectImageButton, 0, 5); // Adjust the row index as needed
        GridPane.setConstraints(selectedImageView, 1, 5); // Adjust the row index as needed

        grid.getChildren().addAll(selectImageButton, selectedImageView);

        Scene scene = new Scene(vbox, SCENE_WIDTH, SCENE_HEIGHT);
        setScene(scene);
        if (this.meal.getName() != null) {
            setTitle(this.meal.getName());
        } else {
            setTitle("Create a new meal");
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

        lastRow = ingredientRow; // Keep track of the last row

        // Add a listener to the unitField to add a new row when it is filled in
        unitField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                addIngredientRow();
            }
        });

        ingredientVBox.getChildren().add(ingredientRow);
    }

    private byte[] imageToByteArray(Image image) {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] byteArray = s.toByteArray();
        return byteArray;
    }

}

