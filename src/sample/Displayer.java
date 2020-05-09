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
import java.util.ResourceBundle;


public class Displayer implements Initializable {
        DbTools tool = new DbTools();

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
            //Need to figure out how to get data out of the Arraylists for each county, state, country
//            deadNumber.setText();
//            recoveredNumber.setText();
//            infectedNumber.setText();

            XYChart.Series confirmedSeries=new XYChart.Series();
            XYChart.Series deathSeries=new XYChart.Series();
            //need the fips number from search;initialize
            double fips=0;

           // HashMap<String,Integer> confirmedDates=getByFips("ConfirmedUS",fips);
            //HashMap<String,Integer> deathDates=getByFips("DeathsUS",fips);
            //while(confirmedDates.hasNext()){
                
            //}
            deathSeries.getData().add(new XYChart.Data("1",23));
            deathSeries.getData().add(new XYChart.Data("1",28));
            lineChart.getData().addAll(confirmedSeries,deathSeries);

        }

}

