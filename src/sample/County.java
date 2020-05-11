package sample;

import jdk.jshell.spi.ExecutionControlProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class County {
    private double fips;
    public String name;
    private HashMap<String, Integer> confirmed;
    private HashMap<String, Integer> deaths;

    public County(double fips, String name,  HashMap<String,Integer> confirmed, HashMap<String,Integer> deaths){
        this.fips = fips;
        this.name = name;
        this.confirmed = confirmed;
        this.deaths = deaths;
    }
    //specify deaths or confirmed with value parameter
    public int getByDate(String value, String date) throws Exception {
        if(value.equals("confirmed")){
            return confirmed.get(date);
        } else if(value.equals("deaths")){
            return deaths.get(date);
        }
        else{
            throw new Exception("Value must be \"confirmed\" or \"deaths\"");
        }
    }

    public ArrayList<Integer> getAll(String value) throws Exception {
        if(value.equals("confirmed")){
            return new ArrayList<Integer>(confirmed.values());
        }else if(value.equals("deaths")){
            return new ArrayList<Integer>(deaths.values());
        }else{
            throw new Exception("Value must be \"confirmed\" or \"deaths\"");
        }
    }

    public String getName(){
        return name;
    }

    public double getFips(){
        return fips;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof County)){
            return false;
        }
        County c = (County)(o);
        return c.getName().equals(name) && c.getFips() == fips;
    }
}
