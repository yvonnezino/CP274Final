package sample;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;

import java.io.IOException;

// Used to add functionality to button and search bar in user interface
public class Controller {
    @FXML
    private Button searchButton;
    @FXML
    private TextField areaSearch;

    public String area;
    DbTools fxToolGetter = new DbTools();



    public String getText() {
        return areaSearch.getText();
    }

    public void buttonCrontrol() throws Exception {
        if (fxToolGetter.getAllCounties().contains(getText())){
            area = getText();
        }
        if (fxToolGetter.getAllStates().contains(getText())){
            area = getText();
        }
        else{
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Window primaryStage = null;
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("No Data for Desired Location"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }

    }



}
