package pt.ipleiria.estg.dei.horadapapa.utilities;

import org.json.JSONException;
import org.json.JSONObject;

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
}
