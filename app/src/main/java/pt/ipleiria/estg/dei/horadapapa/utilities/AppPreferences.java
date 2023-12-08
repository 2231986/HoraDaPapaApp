package pt.ipleiria.estg.dei.horadapapa.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences
{
    private static final String PREFERENCES_NAME = "User_Preferences";
    private static final String USER_TOKEN = "UserToken";
    private static final String API_HOST = "ApiIP";

    private SharedPreferences sharedPreferences;

    public AppPreferences(Context context)
    {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setToken(String token)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getToken()
    {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void setApiIP(String host)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_HOST, host);
        editor.apply();
    }

    public String getApiIP()
    {
        return sharedPreferences.getString(API_HOST, null);
    }

    public void clearPreferences()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

