package sample;


import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

// Used to add functionality to button and search bar in user interface
public class Controller {
    @FXML
    private Button searchButton;
    @FXML
    private TextField areaSearch;
    @FXML
    private Label deadNumber;

    @FXML
    private Label infectedNumber;
    @FXML
    private LineChart lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private PieChart pie;
    @FXML
    private TextField stateSearch;

    public County area;
    DbTools fxToolGetter = new DbTools();


    @FXML
    public String getName() { return areaSearch.getText(); }

    @FXML
    private String getState() {
        return stateSearch.getText();
    }

    public County getCounty() throws Exception {
        return fxToolGetter.getCountyByNameAndState(getName(),getState());
    }

    @FXML
    public void buttonControl() throws Exception {
        if (fxToolGetter.getAllCounties().contains(getCounty())){
            area = getCounty();
        }
        else{
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Window primaryStage = null;
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("No Data for Desired Location"));
            Scene dialogScene = new Scene(dialogVbox, 10, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }

    }



    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if (fxToolGetter.getAllCounties().contains(area)){
                int cur=0;
                ArrayList<County> county = fxToolGetter.getAllCounties();
                int index = county.indexOf(area);
                while (cur != index){
                    cur++;
                }
                deadNumber.setText(Integer.toString(county.get(cur).getAll("deaths").size()));
                infectedNumber.setText(Integer.toString(county.get(cur).getAll("confirmed").size()));
            }
            else{
                int cur=0;
                ArrayList<State> state = fxToolGetter.getAllStates();
                int index = state.indexOf(area);
                while (cur != index){
                    cur++;
                }
                deadNumber.setText(Integer.toString(state.get(cur).deaths));
                infectedNumber.setText(Integer.toString(state.get(cur).cases));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        //create line graph
        XYChart.Series confirmedSeries=new XYChart.Series();
        XYChart.Series deathSeries=new XYChart.Series();
        confirmedSeries.setName("Confirmed Cases");
        deathSeries.setName("Deaths");
        //need the fips number from search;initialize
        double fips=6037.0;

        try {
            Map<String,Integer> confirmedDates=fxToolGetter.getByFips("ConfirmedUS",fips);
            for(Map.Entry<String,Integer> entry:confirmedDates.entrySet()){
                System.out.println("Key: "+entry.getKey()+"Value: "+entry.getValue());
                confirmedSeries.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Map<String,Integer> deathDates=fxToolGetter.getByFips("DeathsUS",fips);
            for(Map.Entry<String,Integer> entry:deathDates.entrySet()){
                deathSeries.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineChart.getData().addAll(confirmedSeries,deathSeries);

    }





}
