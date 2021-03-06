//package CP274Final.src.sample;
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
    private DataRetriever getLiveData=new DataRetriever();
    XYChart.Series confirmedSeries=new XYChart.Series();
    XYChart.Series deathSeries=new XYChart.Series();
    boolean isAddedToGraph=false;

    public County area;
    DbTools fxToolGetter = new DbTools();
    public ArrayList<County> allCounties = fxToolGetter.getAllCounties();

    public Controller() throws Exception {
    }


    @FXML
    // Method to get the county name out of the UI
    public String getName() {
        return areaSearch.getText(); }

    @FXML
    // Method to get the state name out of the UI
    private String getState() {
        return stateSearch.getText();
    }

    // Helper method to get the County object thats associated to the county and state text from the UI
    public County getCounty()throws Exception{
        try {
            return fxToolGetter.getCountyByNameAndState(getName(), getState());
        }
        catch (Exception e) {
            County notARealCounty = new County(0, "County to get exception", null, null);
            return notARealCounty;
        }
    }


    @FXML
    // Updates the UI data when button is clicked
    public void buttonControl() throws Exception {
        // if county name not a real county error message pops up
        if (allCounties.contains(getCounty())){
            area = getCounty();

            int latestNumCases=getLiveData.update(area.getName(),getState(),"confirmed");
            int latestNumDeaths=getLiveData.update(area.getName(),getState(),"deaths");

            // updates the numbers and county name
            countyLabel.setText(area.name);
            infectedNumber.setText(String.valueOf(latestNumCases));
            deadNumber.setText(String.valueOf(latestNumDeaths));

            // Updates the pie charts, presents infected vs dead
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Confirmed Cases",latestNumCases),
                            new PieChart.Data("Deaths",latestNumDeaths));

            pie.setData(pieChartData);

            deathSeries.getData().clear();
            confirmedSeries.getData().clear();
            //sets line graph for desired county
            displayLineGraph(area.getFips());
        }
        else{
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Window primaryStage = null;
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("No Data for Desired Location"));
            Scene dialogScene = new Scene(dialogVbox, 350, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }

    }
    @Override
    // Method that updates the UI at the start of the program, program starts with info from Los Angeles
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int confirmedLa= 0;
        try {
            confirmedLa = getLiveData.update("Los Angeles","California","confirmed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int deathsLa= 0;
        try {
            deathsLa = getLiveData.update("Los Angeles","California","deaths");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // sets labels for number of cases, dead, and county name
            countyLabel.setText(allCounties.get(209).name);
            infectedNumber.setText(String.valueOf(confirmedLa));
            deadNumber.setText(String.valueOf(deathsLa));

        try {
            // sets pie chart
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Confirmed Cases",confirmedLa),
                            new PieChart.Data("Deaths", deathsLa));
            pie.setData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // sets line chart
        displayLineGraph(6037.0);

    }

    // Helper method to update the data in the line graph
    public void displayLineGraph(Double fips){
        //create line graph
        confirmedSeries.setName("Confirmed Cases");
        deathSeries.setName("Deaths");

        try {
            Map<String,Integer> confirmedDates=fxToolGetter.getByFips("ConfirmedUS",fips);
            for(Map.Entry<String,Integer> entry:confirmedDates.entrySet()){
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
        if(isAddedToGraph==false){
            lineChart.getData().addAll(confirmedSeries, deathSeries);
        }
        isAddedToGraph=true;

    }




}
