package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class Controller implements Initializable {
    @FXML
    private Button searchButton;
    @FXML
    private TextField areaSearch;
    @FXML
    private Label deadNumber;
    @FXML
    private Label countyLabel;
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
    public String getName() {
        //System.out.println(areaSearch.getText());
        return areaSearch.getText(); }

    @FXML
    private String getState() {
        //System.out.println(stateSearch.getText());
        return stateSearch.getText();
    }

    public County getCounty() throws Exception {
        return fxToolGetter.getCountyByNameAndState(getName(),getState());
    }

    @FXML
    public void buttonControl() throws Exception {
        if (fxToolGetter.getAllCounties().contains(getCounty())){
            area = getCounty();
            int cur=0;
            ArrayList<County> county = fxToolGetter.getAllCounties();
            int index = county.indexOf(area);
            while (cur != index){
                cur++;
            }
            countyLabel.setText(county.get(cur).name);
            deadNumber.setText(Integer.toString(county.get(cur).getAll("deaths").get(county.get(cur).getAll("deaths").size()-1)));
            infectedNumber.setText(Integer.toString(county.get(cur).getAll("confirmed").get(county.get(cur).getAll("confirmed").size()-1)));
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Infected", county.get(cur).getAll("confirmed").get(county.get(cur).getAll("confirmed").size()-1)),
                            new PieChart.Data("Deaths", county.get(cur).getAll("confirmed").get(county.get(cur).getAll("deaths").size()-1)));
            pie.setData(pieChartData);

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ArrayList<County> county = fxToolGetter.getAllCounties();
            countyLabel.setText(county.get(209).name);
            deadNumber.setText(Integer.toString(county.get(209).getAll("deaths").get(county.get(209).getAll("deaths").size()-1)));
            infectedNumber.setText(Integer.toString(county.get(209).getAll("confirmed").get(county.get(209).getAll("confirmed").size()-1)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ArrayList<County> county = fxToolGetter.getAllCounties();
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Infected", county.get(209).getAll("confirmed").get(county.get(209).getAll("confirmed").size()-1)),
                            new PieChart.Data("Deaths", county.get(209).getAll("confirmed").get(county.get(209).getAll("deaths").size()-1)));
            pie.setData(pieChartData);
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
