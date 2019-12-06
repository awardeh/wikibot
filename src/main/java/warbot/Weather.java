package warbot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.lang.Float.parseFloat;
import static java.lang.Math.round;

public class Weather {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&appid=";
    private static final String UNITS = "&units=metric";
    private static final String INPUTS = "./inputs.txt";


    public static String getWeather(String s) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(INPUTS, true));
        out.write("weather " + s);
        out.newLine();
        JSONObject obj;
        int tempMax;
        int tempMin;
        int temp;
        int humidity;
        String windDir = "";
        String description;
        String country;
        double windDeg;
        String cityName;
        int windSpeed;
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
            windDeg = parseFloat(wind.get("deg").toString());
        } catch (Exception e) {
            windDeg = 0;
        }


        tempMin = round(parseFloat(main.get("temp_min").toString()));
        tempMax = round(parseFloat(main.get("temp_max").toString()));
        temp = round(parseFloat(main.get("temp").toString()));
        humidity = round(parseFloat(main.get("humidity").toString()));
        description = weather2.get("description").toString();
        country = sys.get("country").toString();
        windSpeed = round(parseFloat(wind.get("speed").toString()));
        cityName = obj.get("name").toString();
        byte[] b = cityName.getBytes(StandardCharsets.UTF_8);
        cityName = new String(b, StandardCharsets.UTF_8);

        if (windDeg < 33.75 || windDeg > 348.75)
            windDir = "North";
        if (windDeg < 78.75 && windDeg > 33.75)
            windDir = "North East";
        if (windDeg < 123.75 && windDeg > 78.75)
            windDir = "East";
        if (windDeg < 168.75 && windDeg > 123.75)
            windDir = "South East";
        if (windDeg < 213.75 && windDeg > 168.75)
            windDir = "South";
        if (windDeg < 258.75 && windDeg > 213.75)
            windDir = "South West";
        if (windDeg < 303.75 && windDeg > 258.75)
            windDir = "West";
        if (windDeg < 348.75 && windDeg > 303.75)
            windDir = "North West";


        String result;
        String emojiWeather = "";
        String emojiCountry = ":flag_" + country.toLowerCase() + ":";
        if (description.contains("snow"))
            emojiWeather = "❄";
        if (description.contains("cloud"))
            emojiWeather = "☁️";
        if (description.contains("sun") || description.contains("clear"))
            emojiWeather = "🌞";
        if (description.contains("rain"))
            emojiWeather = "🌧️";
        if (description.contains("haze") || description.contains("fog"))
            emojiWeather = "🌫️";
        String encodedCity = URLEncoder.encode(cityName, "UTF-8");
        result = String.format("%s, %s %s - %s %s\ntemperature: %s° C\nmin: %s° C\nhigh: %s° C\nwind speed: %s km/h direction of %s (%s°)\nHumidity: %s\n",
                cityName, country, emojiCountry, description, emojiWeather, temp, tempMin, tempMax, windSpeed, windDir, windDeg, humidity);
        out.write(result);
        out.newLine();
        out.close();
        System.out.println(result);
        return result;
    }

    private static JSONObject openWeatherCountry(String[] splittedString) throws IOException {
        String encodedString = URLEncoder.encode(splittedString[0], "UTF-8");
        URL url = new URL(API_URL + encodedString + "," + splittedString[1] + API_KEY + UNITS);
        System.out.println(url);
        return getJSON(url);
    }


    private static JSONObject openWeather(String city) throws IOException {
        String encodedString = URLEncoder.encode(city, "UTF-8");
        URL url = new URL(API_URL + encodedString + API_KEY + UNITS);
        System.out.println(url);
        return getJSON(url);
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
            while (sc.hasNext()) inline += sc.nextLine();
            sc.close();
        }
        return new JSONObject(inline);

    }
}
