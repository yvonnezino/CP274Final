//package CP274Final.src.sample;
package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;


public class Displayer implements Initializable {
        DbTools tool = new DbTools();
        Controller displayConrtrol = new Controller();

        @FXML
        private Label deadNumber;
        @FXML
        private Label recoveredNumber;
        @FXML
        private Label infectedNumber;
        @FXML
        private LineChart lineChart;
        @FXML
        private CategoryAxis xAxis;
        @FXML
        private NumberAxis yAxis;
        @FXML
        private PieChart pieChart;



        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                if (tool.getAllCounties().contains(displayConrtrol.area)){
                    int cur=0;
                    ArrayList<County> county = tool.getAllCounties();
                    int index = county.indexOf(displayConrtrol.area);
                    while (cur != index){
                        cur++;
                    }
                    deadNumber.setText(Integer.toString(county.get(cur).getAll("deaths").size()));
                    infectedNumber.setText(Integer.toString(county.get(cur).getAll("confirmed").size()));
                }
                else{
                    int cur=0;
                    ArrayList<State> state = tool.getAllStates();
                    int index = state.indexOf(displayConrtrol.area);
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
                Map<String,Integer> confirmedDates=tool.getByFips("ConfirmedUS",fips);
                for(Map.Entry<String,Integer> entry:confirmedDates.entrySet()){
                    System.out.println("Key: "+entry.getKey()+"Value: "+entry.getValue());
                    confirmedSeries.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Map<String,Integer> deathDates=tool.getByFips("DeathsUS",fips);
                for(Map.Entry<String,Integer> entry:deathDates.entrySet()){
                    deathSeries.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lineChart.getData().addAll(confirmedSeries,deathSeries);

        }

}

