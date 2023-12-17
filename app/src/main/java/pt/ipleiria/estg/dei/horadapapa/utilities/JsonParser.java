package pt.ipleiria.estg.dei.horadapapa.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Dinner;
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

    public static ArrayList<Plate> parseJsonPlates(JSONArray jsonList) {
        ArrayList<Plate> returnList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject obj = jsonList.getJSONObject(i);

                Plate item = new Plate(obj);

                returnList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    public static ArrayList<Dinner> parseJsonDinners(JSONArray jsonList) {
        ArrayList<Dinner> returnList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject obj = jsonList.getJSONObject(i);

                Dinner item = new Dinner(obj);

                returnList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnList;
    }
}
