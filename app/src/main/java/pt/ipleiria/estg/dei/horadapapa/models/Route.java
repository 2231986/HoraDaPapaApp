package pt.ipleiria.estg.dei.horadapapa.models;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

import android.content.Context;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;

public class Route
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

    public static Map<String, String> GetAuthorizationBearerHeader(Context context)
    {
        AppPreferences appPreferences = new AppPreferences(context);
        String bearerToken = appPreferences.getToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);

        return headers;
    }

    public static void HandleApiError( Context context, VolleyError error)
    {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                // Parse the error response JSON
                String errorResponse = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                JSONObject errorJson = new JSONObject(errorResponse);

                // Extract the error message from the JSON
                String errorMessage = errorJson.optString("message", "Unknown Error");
                BetterToast(context, errorMessage);

            } catch (JSONException e) {
                e.printStackTrace();
                BetterToast(context, "Error parsing JSON response");
            }
        } else {
            // If networkResponse or data is null, display a generic error message
            BetterToast(context, "Network error or no response");
        }
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
        String endpoint = ApiPath(context) + "requests/meal/{0}/plate/{1}";
        return MessageFormat.format(endpoint, mealID + "", plateID + ""); //POST - Adiciona um prato à refeição
    }
    public static String MealInvoice(Context context, int mealID){
        String endpoint = ApiPath(context) + "meals/{0}/invoice";
        return MessageFormat.format(endpoint, mealID + ""); //POST - Obtem a fatura da meal
    }
    public static String Invoice(Context context) {
        return Invoice(context, 0);
    }
    public static String Invoice(Context context, int invoiceID) {
        String endpoint = "";

        if (invoiceID == 0){
            endpoint = ApiPath(context) + "invoices"; //GET - Obtem todas as faturas
        }
        else
        {
            endpoint = ApiPath(context) + "invoices/{0}";
            endpoint = MessageFormat.format(endpoint, invoiceID + ""); //CRUD
        }

        return endpoint;
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
        return Dinner(context, 0);
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
        return Helpticket(context, 0);
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
        return Review(context, 0);
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
