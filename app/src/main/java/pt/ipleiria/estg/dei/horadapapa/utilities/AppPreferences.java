package pt.ipleiria.estg.dei.horadapapa.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFERENCES_NAME = "User_Preferences";
    private static final String USER_TOKEN = "UserToken";
    private static final String USER_ID = "0";
    private static final String API_HOST = "ApiIP";
    private static final String MQTT_HOST = "MqttIP";

    private final SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public String getUserID() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void setUserID(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getApiIP() {
        return sharedPreferences.getString(API_HOST, "10.0.2.2:80");
    }

    public void setApiIP(String host) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_HOST, host);
        editor.apply();
    }

    public String getMqttIP() {
        return sharedPreferences.getString(MQTT_HOST, null);
    }

    public void setMqttIP(String host) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MQTT_HOST, host);
        editor.apply();
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

