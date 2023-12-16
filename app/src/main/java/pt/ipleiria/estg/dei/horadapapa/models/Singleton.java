package pt.ipleiria.estg.dei.horadapapa.models;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;
import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.isConnected;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import androidx.fragment.app.Fragment;

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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.MainActivity;
import pt.ipleiria.estg.dei.horadapapa.MealListFragment;
import pt.ipleiria.estg.dei.horadapapa.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.listeners.FavoritesListener;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.JsonParser;

public class Singleton
{
    private static RequestQueue volleyQueue = null;
    private static Singleton singleton_instance = null;
    private static DB_Helper myDatabase;

    private PlatesListener platesListener;
    public void setProdutoListener(PlatesListener platesListener) {
        this.platesListener = platesListener;
    }

    private FavoritesListener favoritesListener;
    public void setFavoritesListener(FavoritesListener favoritesListener) {
        this.favoritesListener = favoritesListener;
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
/*<<<<<<< Updated upstream
=======
                        Intent activity = new Intent(context, MainActivity.class);
                        //context.startActivity(activity);
>>>>>>> Stashed changes*/
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

    public Plate dbGetPlate(int id){
        return myDatabase.getPlate(id);
    }

    /**
     * Pede um prato
     */
    public void requestRequestPlate(Context context, int mealID, int plateID, int quantity, String observation) {
        if (mealID == 0) {
            BetterToast(context, "refeição inválida!");
        }

        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
        }

        if (quantity == 0) {
            BetterToast(context, "quantidade inválida!");
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
                    Route.RequestPlate(context, mealID, plateID),
                    requestBody,
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
     * Adiciona um prato como favorito
     */
    public void requestPlateAddFavorite(Context context, int plateID) {
        if (plateID == 0) {
            BetterToast(context, "prato inválido!");
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
                    ArrayList<Plate> plates = JsonParser.parseJsonPlates(response);

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

    private static class Route
    {
        // Private constructor to prevent instantiation
        private Route() {
            throw new AssertionError("Route class should not be instantiated.");
        }

        private static String ApiHost(Context context) {
            AppPreferences appPreferences = new AppPreferences(context);
            return appPreferences.getApiIP();
        }
        private static String ApiPath(Context context) {
            return  "http://" + ApiHost(context) + "/HoraDaPapa/backend/web/api/";
        }

        public static String UserLogin(Context context) {
            return ApiPath(context) + "user/login"; //GET - Faz login
        }
        public static String UserRegister(Context context) {
            return ApiPath(context) + "user/register"; //POST - Regista o utilizador
        }
        public static String PlateGetAll(Context context) {
            return ApiPath(context) + "plates"; //GET - Obtem todos os pratos
        }
        public static String RequestPlate(Context context, int mealID, int plateID){
            String endpoint = ApiPath(context) + "meals/{0}/plate/{1}";
            return MessageFormat.format(endpoint, mealID + "", plateID + ""); //POST - Adiciona um prato à refeição
        }
        public static String MealInvoice(Context context, int mealID){
            String endpoint = ApiPath(context) + "meals/{0}/invoice";
            return MessageFormat.format(endpoint, mealID + ""); //POST - Obtem a fatura da meal
        }
        public static String MealRequests(Context context, int mealID){
            String endpoint = ApiPath(context) + "meals/{0}/requests";
            return MessageFormat.format(endpoint, mealID + ""); //GET - Obtem os pedidos de uma refeição
        }
        public static String PlateFavorite(Context context) {
            return PlateFavorite(context, 0);
        }
        public static String PlateFavorite(Context context, int plateID) {
            String endpoint = "";

            if (plateID == 0){
                endpoint = ApiPath(context) + "favorites"; //GET - Obtem todos os favoritos
            }
            else
            {
                endpoint = ApiPath(context) + "plates/{0}/favorite";
                endpoint = MessageFormat.format(endpoint, plateID + ""); //POST - Regista ou remove o favorito
            }

            return endpoint;
        }
        public static String Dinner(Context context) {
            return PlateFavorite(context, 0);
        }
        public static String Dinner(Context context, int dinnerID) {
            String endpoint = "";

            if (dinnerID == 0){
                endpoint = ApiPath(context) + "dinners/clean"; //GET - Obtem todos as mesas
            }
            else
            {
                endpoint = ApiPath(context) + "dinners/{0}/start";
                endpoint = MessageFormat.format(endpoint, dinnerID + ""); //POST - Regista uma refeição
            }

            return endpoint;
        }
        public static String Helpticket(Context context) {
            return PlateFavorite(context, 0);
        }
        public static String Helpticket(Context context, int helpticketID) {
            String endpoint = "";

            if (helpticketID == 0){
                endpoint = ApiPath(context) + "helptickets"; //GET - Obtem todos as mesas
            }
            else
            {
                endpoint = ApiPath(context) + "helptickets/{0}";
                endpoint = MessageFormat.format(endpoint, helpticketID + ""); //CRUD
            }

            return endpoint;
        }
        public static String Review(Context context) {
            return PlateFavorite(context, 0);
        }
        public static String Review(Context context, int reviewID) {
            String endpoint = "";

            if (reviewID == 0){
                endpoint = ApiPath(context) + "reviews"; //GET - Obtem todos as mesas
            }
            else
            {
                endpoint = ApiPath(context) + "reviews/{0}";
                endpoint = MessageFormat.format(endpoint, reviewID + ""); //CRUD
            }

            return endpoint;
        }
    }
}