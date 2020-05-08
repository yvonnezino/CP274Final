package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

//Tools for getting data from the database (only works for US data right now (if it works at all))
public class DbTools {
    public static Connection getConnection() throws Exception {
        Connection conn = null;
        // db parameters
        String url = "jdbc:sqlite:TimeDependent.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);

        System.out.println("Connection to SQLite has been established.");

        return conn;
    }

    //Returns all US counties with daily confirmed and death values included
    public static ArrayList<County> getAllCounties() throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT fips, Admin2 FROM DeathsUS");
        ResultSet rs = ps.executeQuery();
        ArrayList<County> result = new ArrayList<County>();
        while (rs.next()) {
            result.add(new County(rs.getDouble("FIPS"), rs.getString("Admin2"),
                    getByFips("ConfirmedUS", rs.getDouble("FIPS")),
                    getByFips("DeathsUS", rs.getDouble("FIPS"))));
        }
        rs.close();
        return result;
    }

    public static ArrayList<State> getAllStates() throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT Province_State FROM DeathsUS");
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
        PreparedStatement ps = con.prepareStatement("SELECT * FROM DeathsUS");
        ResultSet rs = ps.executeQuery();
        ArrayList<County> result = new ArrayList<County>();
        while(rs.next()){
            if(rs.getString("Province_State").equals(stateName)){
                result.add(new County(rs.getDouble("FIPS"), rs.getString("Admin2"),
                        getByFips("ConfirmedUS", rs.getDouble("FIPS")),
                        getByFips("DeathsUS", rs.getDouble("FIPS"))));
            }
        }

        return result;
    }
    //returns a hashmap containing each day linked to the corresponding value for the specified county
    //specify deaths or confirmed cases with field variable
    private static HashMap<String, Integer> getByFips(String field, double fips) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM " + field);
        ResultSet rs = ps.executeQuery();
        HashMap<String, Integer> result = new HashMap<>();

        //this is very hacky, and should probably be done better eventually
        ArrayList<String> days = new ArrayList<>();
        int month = 1;
        int day = 21;
        while(month < 5 || day < 6){
            day++;
            if((day == 32 && month == 1) || (day == 29 && month == 2) || (day == 32 && month == 3)
            || (day == 31 && month == 4)){
                day = 1;
                month++;
            }
            days.add("" + month + "/" + day + "/20");
        }

        while(rs.next()){
            if(rs.getDouble("FIPS") == fips){
                for(int i = 0; i < days.size(); i++){
                    result.put(days.get(i), rs.getInt(days.get(i)));
                }
            }
        }

        return result;
    }
}
