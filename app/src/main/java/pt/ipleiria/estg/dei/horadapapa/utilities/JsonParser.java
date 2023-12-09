package pt.ipleiria.estg.dei.horadapapa.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public class JsonParser {
    private static JSONObject parseRequest(String response) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").contains("success")) {
                return jsonObject.getJSONObject("data");
            } else {
                return jsonObject.getJSONObject("errors");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static String parseJsonLogin(String response) {
        String tokenValue = null;

        try {
            tokenValue = parseRequest(response).getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tokenValue;
    }

    public static ArrayList<Plate> parseJsonPlates(JSONArray platesJsonList) {
        ArrayList<Plate> plateList = new ArrayList<Plate>();

        try {
            for (int i = 0; i < platesJsonList.length(); i++) {
                JSONObject plateObject = platesJsonList.getJSONObject(i);

                Plate plate = new Plate(plateObject);

                plateList.add(plate);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return plateList;
    }
}
