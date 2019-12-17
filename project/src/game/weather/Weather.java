package game.weather;

import game.Game;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Weather implements Game {
    private String cache;
    private boolean done;

    private static final String USER_AGENT = "Mozilla/5.0";

//    private static final String GET_URL_EKB = "https://api.darksky.net/forecast/7f0d053db7dc77cce993237d9df8401e/";
    private static final String GET_URL = "https://api.darksky.net/forecast/7f0d053db7dc77cce993237d9df8401e/";

    public Weather() {
        cache = "input city";
    }

    private String get_weather(String url) throws ParseException {
        String s = getRequest(url);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(s);
        var zone = json.get("timezone");
        var lat = json.get("latitude");
        var lon = json.get("longitude");
        var cur = (JSONObject) json.get("currently");
        var temp = cur.get("temperature");

        StringBuilder sb = new StringBuilder();
        sb.append("Showing weather in ");
        sb.append(zone);
        sb.append(" ");
        sb.append(lat).append(",").append(lon);
        sb.append("\n");
        sb.append("Temperature = ");
        sb.append(Double.parseDouble(temp.toString()) - 32);
        return sb.toString();
    }

    private String getRequest(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String load() {
        return cache;
    }

    @Override
    public String reset() {
        done = false;
        return "Thanks for using this weather add";
    }

    @Override
    public String request(String query) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.opencagedata.com/geocode/v1/json?q=")
                .append(query).append("&key=9de739adec0f485ea9a91bf9a50a7a6e");
        String latlng;
        try {

            latlng = getLatLng(sb.toString());
        }
        catch (Exception e) {
            cache = "input city";
            return "Wrong city";
        }
        String l;
        try {
            l = get_weather(GET_URL + latlng);
        } catch (Exception e) {
            cache = "input city";
            return "Wrong city";
        }
        return l;
    }

    private String getLatLng(String url) throws ParseException {
        String res = getRequest(url);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(res);
        var results = (JSONArray) json.get("results");
        var ob = (JSONObject) results.get(0);
        var geom = (JSONObject) ob.get("geometry");
        var lat = geom.get("lat");
        var lng = geom.get("lng");
        return lat.toString() + "," + lng.toString();
    }

    @Override
    public Boolean isFinished() {
        return done;
    }
}
