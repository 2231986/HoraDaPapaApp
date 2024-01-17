package pt.ipleiria.estg.dei.horadapapa.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.LoginInfo;

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

    public static LoginInfo parseJsonLogin(String response) {
        String tokenValue = null;
        String userIdValue = null;

        try {
            JSONObject json = parseRequest(response);
            tokenValue = json.getString("token");
            userIdValue = json.getString("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new LoginInfo(tokenValue, userIdValue);
    }

    public static <T> T parseGenericObject(JSONObject jsonObject, Class<T> type) {
        try {
            return type.getDeclaredConstructor(JSONObject.class).newInstance(jsonObject);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> ArrayList<T> parseGenericList(JSONArray jsonList, Class<T> type) {
        ArrayList<T> returnList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject obj = jsonList.getJSONObject(i);

                T item = type.getDeclaredConstructor(JSONObject.class).newInstance(obj);

                returnList.add(item);
            }

        } catch (JSONException | ReflectiveOperationException e) {
            e.printStackTrace();
            Log.e("JsonParser", "Error: parseGenericList", e);
        }

        return returnList;
    }
}