package sample;

public class County {
    private int fips;
    private String name;
    private int confirmed;
    private int deaths;
    private int recovered;

    public County(int fips, String name, int confirmed, int deaths, int recovered){
        this.fips = fips;
        this.name = name;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
    }


}
