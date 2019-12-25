package warbot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.lang.Float.parseFloat;
import static java.lang.Math.pow;
import static java.lang.Math.round;

public class Weather implements Logger {
    private static final String API_SITE = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&appid=";
    private static final String UNITS = "&units=metric";

    public static String getWeather(String s) throws IOException {
        Logger.logInput("temp " + s);

        Path path = Paths.get("./weathertoken"); //the path of the weather token file should be in the project folder or in the jar folder
        String weatherToken; // initializes bot token
        weatherToken = Files.readString(path); //reads the weather api

        JSONObject obj;
        String windDir = "";
        String description;
        String country;
        String cityName;
        float windDeg;
        float tempMin;
        float tempMax;
        float humidity;
        float temp;
        float windSpeed;
        double windChill;

        if (s.contains(",")) {
            String[] formattedInput = s.trim().split(",");
            formattedInput[0] = formattedInput[0].trim();
            formattedInput[1] = formattedInput[1].trim();
            obj = openWeatherCountry(formattedInput, weatherToken);
        } else {
            String input = s.trim();
            obj = openWeather(input, weatherToken);
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

        tempMin = parseFloat(main.get("temp_min").toString());
        tempMax = parseFloat(main.get("temp_max").toString());
        temp = parseFloat(main.get("temp").toString());
        windSpeed = parseFloat(wind.get("speed").toString());
        windChill = 35.74 + 0.6215 * temp - 35.75 * pow(windSpeed, 0.16) + 0.3965 * temp * Math.pow(windSpeed, 0.16);
        humidity = parseFloat(main.get("humidity").toString());
        description = weather2.get("description").toString();
        country = sys.get("country").toString();
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
        String output = String.format("%s, %s %s - %s %s\nTemperature: %s° C\nFeels like: %s° C\nLow: %s° C\nHigh: %s° C\nWind speed: %s km/h direction of %s (%s°)\nHumidity: %s\n",
                cityName, country, emojiCountry, description, emojiWeather, round(temp), round(windChill), round(tempMin), round(tempMax), round(windSpeed), windDir, round(windDeg), humidity);
        Logger.logOutput(output);
        return output;
    }


    private static JSONObject openWeatherCountry(String[] splitString, String weatherToken) throws IOException {
        String encodedString = URLEncoder.encode(splitString[0], StandardCharsets.UTF_8);
        URL url = new URL(API_SITE + encodedString + "," + splitString[1] + API_KEY + weatherToken + UNITS);
        return getJSON(url);
    }


    private static JSONObject openWeather(String city, String weatherToken) throws IOException {
        String encodedString = URLEncoder.encode(city, StandardCharsets.UTF_8);
        URL url = new URL(API_SITE + encodedString + API_KEY + weatherToken + UNITS);
        return getJSON(url);
    }


    private static JSONObject getJSON(URL url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        StringBuilder inline = new StringBuilder();
        int responseCode = conn.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException("not found");
        }
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) inline.append(sc.nextLine());
            sc.close();
        }
        return new JSONObject(inline.toString());

    }

    public static void main(String[] args) throws IOException {
        String string = getWeather("riyadh");
        System.out.println(string);
    }
}
