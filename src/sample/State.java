package sample;

import java.util.ArrayList;

public class State {
    private String name;
    private ArrayList<County> counties;
    public int deaths;
    public int cases;

    public State(String name, ArrayList<County> counties){
        this.name = name;
        this.counties = counties;
    }

    public ArrayList<County> getCounties(){
        return counties;
    }

    public void getDeaths() throws Exception {
        DbTools tool = new DbTools();
        int cur = 0;
        for (int i=0; i <tool.getCountiesByStateName(name).size(); i++){
            County countyName = tool.getCountiesByStateName(name).get(i);
            cur += countyName.getAll("deaths").size();
        }
        deaths = cur;
    }

    public void getCases() throws Exception {
        DbTools tool = new DbTools();
        int cur = 0;
        for (int i=0; i <tool.getCountiesByStateName(name).size(); i++){
            County countyName = tool.getCountiesByStateName(name).get(i);
            cur += countyName.getAll("confirmed").size();
        }
        cases = cur;
    }


}
