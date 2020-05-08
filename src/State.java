package sample;

import java.util.ArrayList;

public class State {
    private String name;
    private ArrayList<County> counties;

    public State(String name, ArrayList<County> counties){
        this.name = name;
        this.counties = counties;
    }

    public ArrayList<County> getCounties(){
        return counties;
    }
}
