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

    XYChart.Series confirmedSeries=new XYChart.Series();
    XYChart.Series deathSeries=new XYChart.Series();
    boolean isAddedToGraph=false;

    public County area;
    DbTools fxToolGetter = new DbTools();
    public ArrayList<County> allCounties = fxToolGetter.getAllCounties();

    public Controller() throws Exception {
    }


    @FXML
    public String getName() {
        return areaSearch.getText(); }

    @FXML
    private String getState() {
        return stateSearch.getText();
    }

    public County getCounty() throws Exception {
        return fxToolGetter.getCountyByNameAndState(getName(),getState());
    }

    @FXML
    public void buttonControl() throws Exception {
        if (allCounties.contains(getCounty())){
            area = getCounty();
            int cur=0;
            //ArrayList<County> county = fxToolGetter.getAllCounties();
            int index = allCounties.indexOf(area);
            while (cur != index){
                cur++;
            }
            String latestNumDeaths=Integer.toString(allCounties.get(cur).getAll("deaths").get(allCounties.get(cur).getAll("deaths").size()-1));
            String latestNumCases=Integer.toString(allCounties.get(cur).getAll("confirmed").get(allCounties.get(cur).getAll("confirmed").size()-1));

            countyLabel.setText(allCounties.get(cur).name);
            infectedNumber.setText(latestNumCases);
            deadNumber.setText(latestNumDeaths);

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Confirmed Cases",Integer.parseInt(latestNumCases)),
                            new PieChart.Data("Deaths",Integer.parseInt(latestNumDeaths)));

            pie.setData(pieChartData);

            deathSeries.getData().clear();
            confirmedSeries.getData().clear();
            displayLineGraph(allCounties.get(cur).getFips());
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
        String confirmedLa="";
        String deathsLa="";
        try {
            confirmedLa = Integer.toString(allCounties.get(209).getAll("confirmed").get(allCounties.get(209).getAll("confirmed").size() - 1));
            deathsLa = Integer.toString(allCounties.get(209).getAll("deaths").get(allCounties.get(209).getAll("deaths").size() - 1));
        }
        catch (Exception e) {
                e.printStackTrace();
        }
            countyLabel.setText(allCounties.get(209).name);
            infectedNumber.setText(confirmedLa);
            deadNumber.setText(deathsLa);

        try {
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Confirmed Cases",Integer.parseInt(confirmedLa)),
                            new PieChart.Data("Deaths", Integer.parseInt(deathsLa)));
            pie.setData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayLineGraph(6037.0);

    }

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
