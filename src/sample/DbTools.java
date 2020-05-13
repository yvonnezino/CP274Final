package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//Tools for getting data from the database (only works for US data right now (if it works at all))
public class DbTools {
    static Connection conn = null;
    public static Connection getConnection() throws Exception {
        if(conn!=null){
            return conn;
        }
        // db parameters
        String url="jdbc:sqlite:TimeDependent.db";
        //String url = "jdbc:sqlite:/Users/yvonnezino/IdeaProjects/CP274Final/src/CP274Final/TimeDependent.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);

        //System.out.println("Connection to SQLite has been established.");

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

    // Returns all US States
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
        rs.close();
        return result;
    }

    // Returns list of counties based on inputted state name
    public static ArrayList<County> getCountiesByStateName(String stateName) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM DeathsUS WHERE province_state =  ?");
        ps.setString(1,stateName);
        ResultSet rs = ps.executeQuery();
        ArrayList<County> result = new ArrayList<County>();
        while(rs.next()){
                result.add(new County(rs.getDouble("FIPS"), rs.getString("Admin2"),
                        getByFips("ConfirmedUS", rs.getDouble("FIPS")),
                        getByFips("DeathsUS", rs.getDouble("FIPS"))));
        }
        rs.close();
        return result;
    }



    //returns a county object of given name and state
    public County getCountyByNameAndState(String name, String stateName) throws Exception {
        ArrayList<County> counties = getCountiesByStateName(stateName);
        for(int i = 0; i < counties.size(); i++){
            if(counties.get(i).getName().equals(name)){
                return counties.get(i);
            }
        }
        throw new Exception("county not found");
    }


    //returns a hashmap containing each day linked to the corresponding value for the specified county
    //specify deaths or confirmed cases with field variable
    public static LinkedHashMap<String, Integer> getByFips(String field, double fips) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM " + field + " WHERE fips = ?");
        ps.setDouble(1, fips);
        ResultSet rs = ps.executeQuery();
        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();

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
                for(int i = 0; i < days.size(); i++){
                    result.put(days.get(i), rs.getInt(days.get(i)));
                }
        }
        rs.close();
        return result;
    }

}

