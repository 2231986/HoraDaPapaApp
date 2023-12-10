package pt.ipleiria.estg.dei.horadapapa.models;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;
import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.isConnected;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.MainActivity;
import pt.ipleiria.estg.dei.horadapapa.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.JsonParser;

public class Singleton
{
    public static String ApiHost = "10.0.2.2:8888";
    private static RequestQueue volleyQueue = null;
    private static Singleton singleton_instance = null;
    private static DB_Helper myDatabase;

    private PlatesListener platesListener;
    public void setProdutoListener(PlatesListener platesListener) {
        this.platesListener = platesListener;
    }

    private Singleton(Context context)
    {
        AppPreferences appPreferences = new AppPreferences(context);
        String apiHost = appPreferences.getApiIP();

        if (apiHost != null && !apiHost.isEmpty())
        {
            Singleton.ApiHost = apiHost;
        }

        volleyQueue = Volley.newRequestQueue(context);
        myDatabase = new DB_Helper(context);
    }

    public static synchronized Singleton getInstance(Context context)
    {
        if (singleton_instance == null)
        {
            singleton_instance = new Singleton(context);
        }

        return singleton_instance;
    }

    public void requestUserLogin(final Context context, User user) {

        String credentials = user.getUsername() + ":" + user.getPassword();
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Route.UserLogin, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String userToken = JsonParser.parseJsonLogin(response);

                    if (userToken == null) {
                        BetterToast(context, "Ocorreu um erro!");
                    }
                    else
                    {
                        AppPreferences appPreferences = new AppPreferences(context);
                        appPreferences.setToken(userToken);

                        BetterToast(context, "Login efetuado com sucesso!");

                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BetterToast(context, "Username ou Password errados!");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + base64EncodedCredentials);
                    return headers;
                }
            };

            volleyQueue.add(stringRequest);
        }
    }

    public void requestUserRegister(final Context context, User user) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Route.UserRegister,
                response -> {
                    if (response.contains("success")) {
                        BetterToast(context, "Signed up!");
                        Intent activity = new Intent(context, MainActivity.class);
                        context.startActivity(activity);
                    } else {
                        BetterToast(context, response);
                    }
                },
                error -> {
                    String errorMessage;

                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String data = new String(error.networkResponse.data);
                        errorMessage = "Server error - Status Code: " + statusCode + "\n" + data;
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "Request timed out. Please try again.";
                    } else if (error instanceof NoConnectionError) {
                        errorMessage = "No internet connection. Please check your network settings.";
                    } else {
                        errorMessage = "An unknown error occurred.";
                    }

                    BetterToast(context, errorMessage);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());
                params.put("email", user.getEmail());
                return params;
            }
        };

        Singleton.getInstance(context).volleyQueue.add(stringRequest);
    }

    public void requestPlateGetAll(Context context) {
        if(!isConnected(context)){
            BetterToast(context,"Sem internet!");

            ArrayList<Plate> plates = myDatabase.getPlates();

            if (platesListener != null){
                platesListener.onRefreshPlates(plates);
            }else{
                BetterToast(context,"Ocorreu um erro ao colocar no Listener!");
            }
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.PlateGetAll, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Plate> plates = JsonParser.parseJsonPlates(response);
                    myDatabase.setPlates(plates);

                    if (platesListener != null) {
                        platesListener.onRefreshPlates(plates);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    BetterToast(context, "Ocorreu um erro!");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    AppPreferences appPreferences = new AppPreferences(context);
                    String bearerToken = appPreferences.getToken();

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + bearerToken);
                    return headers;
                }
            };

            volleyQueue.add(jsonArrayRequest);
        }
    }

    private static class Route
    {
        public static String ApiPath = "http://" + ApiHost + "/HoraDaPapa/backend/web/api/";
        public static String UserLogin = ApiPath + "user/login"; //GET - Faz login
        public static String UserRegister = ApiPath + "user/register"; //POST - Regista o utilizador
        public static String PlateGetAll = ApiPath + "plates"; //GET - Obtem todos os pratos
    }
}