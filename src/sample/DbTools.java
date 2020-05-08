package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
//Tools for getting data from the database
public class DbTools {
    public static Connection getConnection() throws Exception {
        Connection conn = null;
        // db parameters
        String url = "jdbc:sqlite:LatestDataSet.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);

        System.out.println("Connection to SQLite has been established.");

        return conn;
    }
    //Returns all counties
    public static ArrayList<County> getAllCounties() throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT fips, Admin2, Confirmed, Deaths," +
                " Recovered FROM LatestDataSet");
        ResultSet rs = ps.executeQuery();
        ArrayList<County> result = new ArrayList<County>();
        while (rs.next()) {
            if(rs.getString("Admin2") != null) {
                result.add(new County(rs.getInt("fips"), rs.getString("Admin2"),
                        rs.getInt("Confirmed"), rs.getInt("Deaths"),
                        rs.getInt("Recovered")));
            }
        }
        rs.close();
        return result;
    }
    //not done
    //if there are multiple countries with provinces of the same name, this might not work
    public static ArrayList<State> getAllStates() throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT Province_State FROM LatestDataSet");
        ResultSet rs = ps.executeQuery();

        ArrayList<State> result = new ArrayList<State>();

        ArrayList<String> uniqueStateNames = new ArrayList<String>();
        while(rs.next()){
            if(!uniqueStateNames.contains(rs.getString("Province_State"))){
                uniqueStateNames.add(rs.getString("Province_State"));
                result.add(new State(rs.getString("Province_State"),
                        getCountiesByStateName(rs.getString("Province_State"))));
            }
        }
        return result;
    }

    private static ArrayList<County> getCountiesByStateName(String stateName) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT fips, Admin2, Confirmed, Deaths," +
                " Recovered, Province_State FROM LatestDataSet");
        ResultSet rs = ps.executeQuery();
        ArrayList<County> result = new ArrayList<County>();
        while(rs.next()){
            if(rs.getString("Province_State").equals(stateName)){
                result.add(new County(rs.getInt("fips"), rs.getString("Admin2"),
                        rs.getInt("Confirmed"), rs.getInt("Deaths"),
                        rs.getInt("Recovered")));
            }
        }

        return result;
    }

    //TODO:
    //public static ArrayList getAllCountries(){}
}
