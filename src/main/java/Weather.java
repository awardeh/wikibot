import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Weather {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&appid=";
    private static final String UNITS = "&units=metric";
    private static final String INPUTS = "./inputs.txt";


    public static String getWeather(String s) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("weather " + s);
        out.newLine();
        out.close();

        JSONObject obj;
        String tempMax;
        String tempMin;
        String temp;
        String humidity;
        String description;
        String country;
        String windDeg;
        String cityName;
        String windSpeed;
        if (s.contains(",")) {
            String[] splittedString = s.trim().split(",");
            splittedString[0] = splittedString[0].trim();
            splittedString[1] = splittedString[1].trim();
            obj = openWeatherCountry(splittedString);
        } else {
            String input = s.trim();
            obj = openWeather(input);
        }
        JSONObject main = (JSONObject) obj.get("main");
        JSONArray weather = (JSONArray) obj.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject wind = (JSONObject) obj.get("wind");
        JSONObject sys = (JSONObject) obj.get("sys");
        try {
            windDeg = wind.get("deg").toString();
        } catch (Exception e) {
            windDeg = "N/A";
        }

        tempMin = main.get("temp_min").toString();
        tempMax = main.get("temp_max").toString();
        temp = main.get("temp").toString();
        humidity = main.get("humidity").toString();
        description = weather2.get("description").toString();
        country = sys.get("country").toString();
        windSpeed = wind.get("speed").toString();
        cityName = obj.get("name").toString();
        byte[] b = cityName.getBytes("UTF-8");
        cityName = new String(b, "UTF-8");
        String emoji;

        String result = String.format("%s, %s - %s\ntemperature: %s\nmin: %s\nhigh: %s\nwind speed: %s direction of %s degrees\nHumidity: %s\n",
                cityName, country, description, temp, tempMin, tempMax, windSpeed, windDeg, humidity);
        System.out.println(result);
        return result;
    }

    private static JSONObject openWeatherCountry(String[] splittedString) throws IOException {
        String encodedString = URLEncoder.encode(splittedString[0], "UTF-8");
        URL url = new URL(API_URL + encodedString + "," + splittedString[1] + API_KEY + UNITS);
        System.out.println(url);
        JSONObject obj = getJSON(url);
        return obj;
    }


    private static JSONObject openWeather(String city) throws IOException {
        String encodedString = URLEncoder.encode(city, "UTF-8");
        URL url = new URL(API_URL + encodedString + API_KEY + UNITS);
        System.out.println(url);
        JSONObject obj = getJSON(url);
        return obj;
    }

    public static void main(String[] args) throws IOException {
        String test = getWeather(" Linköping");
        System.out.println(test);
    }

    private static JSONObject getJSON(URL url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        int responsecode = conn.getResponseCode();
        if (responsecode == 404) {
            throw new IllegalArgumentException("not found");
        }
        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();
        }
        return new JSONObject(inline);

    }
}
