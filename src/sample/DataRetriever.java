package sample;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//https://coronavirus-tracker-api.herokuapp.com/#/v2/get_locations_v2_locations_get

public class DataRetriever {
    //returns current specified value for specified county, pulled from api
    public static int update(String county, String state, String value) throws Exception {
        // Host url
        String host = "https://coronavirus-tracker-api.herokuapp.com/v2/";
        String response = Unirest.get("" + host + "locations?source=csbs&country_code=US&" +
                "province=" + URLEncoder.encode(state, "UTF-8") +
                "&county=" + URLEncoder.encode(county, "UTF-8") +
                "&timelines=true")
                .header("accept", "application/json")
                .asJson()
                .getBody()
                .toString();
        if(value.equals("confirmed")){
            return Integer.parseInt(response.substring(response.indexOf("confirmed") + 11,response.indexOf("deaths") - 2));
        } else if(value.equals("deaths")){
            String s = response.substring(response.lastIndexOf("deaths") + 8);
            return Integer.parseInt(s.substring(0, s.length() - 2));
        }
        throw new Exception("invalid value");
    }
}
