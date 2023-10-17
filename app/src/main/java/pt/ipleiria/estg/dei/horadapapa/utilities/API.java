package pt.ipleiria.estg.dei.horadapapa.utilities;

import static pt.ipleiria.estg.dei.horadapapa.models.Singleton.isConnected;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class API {
    public static String MainPath = "http://localhost/HoraDaPapa";
    public static String UserLogin = MainPath + "/backend/web/api/user/login";
    public static String UserRegister = MainPath + "/backend/web/api/user/register";

    public static void userLogin(Context context, String username, String password) {
        if (!isConnected(context)) {
            Toast.makeText(context, "Sem internet!", Toast.LENGTH_SHORT).show();
        } else {
            String credentials = username + ":" + password;
            String basicAuth = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, API.UserLogin, response -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    editor.putString("UserToken", jsonObject.getString("response"));
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Login com sucesso!", Toast.LENGTH_SHORT).show();

            }, error -> Toast.makeText(context, "Credenciais inválidas!", Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + basicAuth);
                    return headers;
                }
            };

            Singleton.volleyQueue.add(stringRequest);
        }
    }
}