//package CP274Final.src.sample;
package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("tracker.fxml"));
        primaryStage.setTitle("Chart View");
        primaryStage.setScene(new Scene(root, 990, 900));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

