package pl.rest.kantor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class GsonReader {



    public static LinkedHashMap<String, Double> danePobraneZApi() {
        String dane = GsonReader.jsonGetRequest();
        LinkedHashMap<String, Double> lhm = GsonReader.stringDataToLhm(dane);
        return lhm;
    }

    public static LinkedHashMap<String, Double> stringDataToLhm(String input) {
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        String[] array = input.split(",");
        for (String s : array) {
            String[] a = s.split(":");
            map.put(a[0], Double.parseDouble(a[1]));
        }
        return map;
    }

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public static String jsonGetRequest() {
        String json = null;
        try {
            URL url = new URL("https://api.ratesapi.io/api/latest?base=PLN");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json.replaceAll("[^A-Za-z0-9.:,-]", "").replaceAll("base:.*rates:", "").replaceAll(",date:.*", "");
    }
}
