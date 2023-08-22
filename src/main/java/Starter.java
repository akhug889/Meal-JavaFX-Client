import javafx.application.Application;

public class Starter {

    // Constructor
    private Starter() {

    }

    // Methods
    public static void main(String[] args) {
        //This is a workaround for a known issue when starting JavaFX applications
        MealClientApplication.startApp(args);
    }

}
