package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

        }
    }

