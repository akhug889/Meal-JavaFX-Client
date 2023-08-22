import controller.MealAppController;
import controller.MealAppRepository;
import javafx.application.Application;
import javafx.stage.Stage;
import rest.MealAppWebRepository;
import view.MealListView;

public class MealClientApplication extends Application {

    // Methods
    public static void startApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    // Instantiate Web Repository, Repository and Controller
    MealAppWebRepository mealAppWebRepository = new MealAppWebRepository();
    MealAppRepository mealAppRepository = new MealAppRepository(mealAppWebRepository);
    MealAppController mealAppController = new MealAppController(mealAppRepository);

    MealListView mealListView = new MealListView(mealAppController);
    mealListView.show();


    }
}
