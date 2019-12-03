import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&appid=MYID";
    private static final String UNITS = "&units=metric";


    public static String getWeather(String s) throws IOException {

//        if (s.contains(" ")) {
//            String[] splittedString = s.split(" ");
//            openWeatherCountry(splittedString);
//        } else {
        JSONObject obj = openWeather(s);
        JSONObject main = (JSONObject) obj.get("main");
        JSONArray weather = (JSONArray) obj.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject wind = (JSONObject) obj.get("wind");
        JSONObject sys = (JSONObject) obj.get("sys");


        String temp = main.get("temp").toString();
        ;
        String tempMin = main.get("temp_min").toString();
        ;
        String tempMax = main.get("temp_max").toString();
        ;
        String humidity = main.get("humidity").toString();
        String description = weather2.get("description").toString();
        String country = sys.get("country").toString();
        String windSpeed = wind.get("speed").toString();
        String windDeg = wind.get("deg").toString();
        String cityName = obj.get("name").toString();

        String result = String.format("%s, %s - %s\ntemperature: %s\nmin: %s\nhigh: %s\nwind speed: %s direction of %s degrees\nHumidity: %s\n", cityName, country, description, temp, tempMin, tempMax, windSpeed, windDeg, humidity);
        System.out.println(result);
        return result;
    }

//    private void openWeatherCountry(String[] splittedString) throws IOException {
//        URL url = new URL(API_URL + splittedString[0] + "," + splittedString[1] + API_KEY);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.connect();
//        int responsecode = conn.getResponseCode();
//        String inline = "";
//        if (responsecode != 200) {
//            throw new RuntimeException("HttpResponseCode: " + responsecode);
//        } else {
//            Scanner sc = new Scanner(url.openStream());
//            while (sc.hasNext()) {
//                inline += sc.nextLine();
//            }
//            System.out.println("\nJSON data in string format");
//            System.out.println(inline);
//            sc.close();
//            JSONObject obj = new JSONObject(inline);
//            System.out.println(obj.toString());
//
//        }


    private static JSONObject openWeather(String city) throws IOException {
        URL url = new URL(API_URL + city + API_KEY + UNITS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();
            JSONObject obj = new JSONObject(inline);
            return obj;
        }
    }

    public static void main(String[] args) throws IOException {
        String test = getWeather("Toronto");
        System.out.println(test);
    }
}