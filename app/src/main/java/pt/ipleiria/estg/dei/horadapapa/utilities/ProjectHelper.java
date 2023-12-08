package pt.ipleiria.estg.dei.horadapapa.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectHelper {
    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void BetterToast(Context context, String message) {
        JSONObject jsonObject = null;

        try
        {
            jsonObject = new JSONObject(message);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (jsonObject != null) //is JSON
        {
            try
            {
                if (jsonObject.getString("status").contains("success"))
                {
                    String msgContent = jsonObject.getString("message");
                    Toast.makeText(context, msgContent, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONArray errorsArray = jsonObject.getJSONArray("errors");

                    if (errorsArray.length() > 0) {

                        JSONObject firstError = errorsArray.getJSONObject(0);

                        String firstErrorProperty = firstError.keys().next();

                        JSONArray firstErrorTextArray = firstError.getJSONArray(firstErrorProperty);

                        Toast.makeText(context, firstErrorTextArray.getString(0), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(context, "Ocorreu um erro ao interpertar o pedido!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else //is String
        {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isUsernameValid(String username) {
        //TODO: Colocar validações melhores, talvez usar as regras do Yii2
        if (username == null || username.length() <= 2)
            return false;
        else
            return true;
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Colocar validações melhores, talvez usar as regras do Yii2
        if (password == null || password.length() <= 2)
            return false;
        else
            return true;
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNifValid(String nif){
        return nif.matches("[0-9]{9}");
    }
}