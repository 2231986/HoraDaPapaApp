package pt.ipleiria.estg.dei.horadapapa.models;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;
import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.isConnected;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.listeners.DinnersListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.FavoritesListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.JsonParser;

public class Singleton
{
    private static RequestQueue volleyQueue = null;
    private static Singleton singleton_instance = null;
    private static DB_Helper myDatabase;

    private static int currentMealID = 0; //Guarda o ID da Meal atual

    private PlatesListener platesListener;
    public void setPlatesListener(PlatesListener platesListener) {
        this.platesListener = platesListener;
    }

    private FavoritesListener favoritesListener;
    public void setFavoritesListener(FavoritesListener favoritesListener) {
        this.favoritesListener = favoritesListener;
    }

    private DinnersListener dinnersListener;
    public void setDinnersListener(DinnersListener dinnersListener) {
        this.dinnersListener = dinnersListener;
    }

    private Singleton(Context context)
    {
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
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Route.UserLogin(context), new Response.Listener<String>() {
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

                        //TODO: Vai ter de se substituir o Intent por um Listener!
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Route.UserRegister(context),
                response -> {
                    if (response.contains("success")) {
                        BetterToast(context, "Signed up!");

                        //TODO: Vai ter de se substituir o Intent por um Listener!
                        ((Activity) context).finish();
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
                params.put("name", user.getName());
                params.put("surname", user.getSurname());
                params.put("nif", user.getNif());
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
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.PlateGetAll(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Plate> plates = JsonParser.parseGenericList(response, Plate.class);
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

    public void requestMealRequests(Context context) {
        if (currentMealID == 0) {
            BetterToast(context, "refeição inválida!");
            return;
        }

        if(!isConnected(context)){
            BetterToast(context,"Sem internet!");
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.MealRequests(context, currentMealID), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<PlateRequest> requests = JsonParser.parseGenericList(response, PlateRequest.class);
                    ArrayList<Plate> plates = new ArrayList<>();

                    for (PlateRequest request : requests) {
                        int plateId = request.getPlateId();
                        Plate plate = dbGetPlate(plateId);
                        plates.add(plate);
                    }

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

    public Plate dbGetPlate(int id){
        return myDatabase.getPlate(id);
    }

    /**
     * Pede um prato
     */
    public void requestRequestPlate(Context context, int plateID, int quantity, String observation) {
        if (currentMealID == 0) {
            BetterToast(context, "refeição inválida!");
            return;
        }

        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (quantity == 0) {
            BetterToast(context, "quantidade inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JSONObject requestBody = new JSONObject();

            try {
                if (quantity > 0){
                    requestBody.put("quantity", quantity);
                }
                if (observation != null && !observation.isEmpty()){
                    requestBody.put("observation", observation);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Route.RequestPlate(context, currentMealID, plateID),
                    requestBody,
                    response -> {
                        Toast.makeText(context, "O pedido foi feito!", Toast.LENGTH_SHORT).show();
                        // TODO: Implement response
                    },
                    error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {
                    AppPreferences appPreferences = new AppPreferences(context);
                    String bearerToken = appPreferences.getToken();

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + bearerToken);
                    return headers;
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    /**
     * Adiciona um prato como favorito
     */
    public void requestPlateAddFavorite(Context context, int plateID) {
        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Route.PlateFavorite(context, plateID),null,
                    response -> {
                        // TODO: Implement response
                    },
                    error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {
                    AppPreferences appPreferences = new AppPreferences(context);
                    String bearerToken = appPreferences.getToken();

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + bearerToken);
                    return headers;
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    /**
     * Remove um prato como favorito
     */
    public void requestPlateRemoveFavorite(Context context, int plateID) {
        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.DELETE,
                    Route.PlateFavorite(context, plateID),null,
                    response -> {
                        // TODO: Implement response
                    },
                    error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {
                    AppPreferences appPreferences = new AppPreferences(context);
                    String bearerToken = appPreferences.getToken();

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + bearerToken);
                    return headers;
                }
            };

            volleyQueue.add(jsonObjectRequest);
        }
    }

    public void requestFavoritesGetAll(Context context) {
        if(!isConnected(context)){
            BetterToast(context,"Sem internet!");
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.PlateFavorite(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Plate> plates = JsonParser.parseGenericList(response, Plate.class);

                    if (plates != null) {
                        favoritesListener.onRefreshFavorites(plates);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {

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

    public void requestDinnerGetAll(Context context) {
        if(!isConnected(context)){
            BetterToast(context,"Sem internet!");
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Route.Dinner(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    ArrayList<Dinner> dinners = JsonParser.parseGenericList(response, Dinner.class);

                    if (dinners != null) {
                        dinnersListener.onRefreshDinners(dinners);
                    } else {
                        BetterToast(context, "Ocorreu um erro ao colocar no Listener!");
                    }
                }
            }, error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {

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

    public void requestDinnerStart(Context context, int dinnerID) {
        if (dinnerID == 0) {
            BetterToast(context, "Mesa inválida!");
            return;
        }

        if (!isConnected(context)) {
            BetterToast(context, "Sem internet!");
        } else {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Route.Dinner(context, dinnerID),null,
                    response -> {
                        Meal meal = JsonParser.parseGenericObject(response, Meal.class);
                        if (meal != null){
                            currentMealID = meal.getId();
                        }
                    },
                    error -> BetterToast(context, "Ocorreu um erro!")) {
                @Override
                public Map<String, String> getHeaders() {
                    AppPreferences appPreferences = new AppPreferences(context);
                    String bearerToken = appPreferences.getToken();

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + bearerToken);
                    return headers;
                }
            };

            volleyQueue.add(request);
        }
    }

    public ArrayList<Plate> filterPlatesByContent(String keyword) {
        ArrayList<Plate> plates = myDatabase.getPlates();

        if (keyword == null || keyword.isEmpty()) {
            return plates;
        }

        ArrayList<Plate> filteredPlates = new ArrayList<>();

        for (Plate plate : plates) {
            if (plate.getTitle().contains(keyword) || plate.getDescription().contains(keyword)) {
                filteredPlates.add(plate);
            }
        }

        return filteredPlates;
    }
}