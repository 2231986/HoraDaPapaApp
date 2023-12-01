package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    String credentials = "MarrianaPonte" + ":" + "12345678";
    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.editText_username);
        etPassword = findViewById(R.id.editText_password);
    }
    private boolean isUsernameValid(String content) {
        //TODO: Colocar validações melhores, talvez usar as regras do Yii2
        if (content.length() <= 2)
            return false;
        else
            return true;
    }

    private boolean isPasswordValid(String content) {
        //TODO: Colocar validações melhores, talvez usar as regras do Yii2
        if (content.length() <= 2)
            return false;
        else
            return true;
    }

    public void onClickCheckLogin(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        if (!isUsernameValid(username) || !isPasswordValid(password)) {
            Toast.makeText(this, "Dados de acesso inválidos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make a Volley POST request to your API endpoint for user authentication
        String loginUrl = "http://10.0.2.2:8888/HoraDaPapa/backend/web/api/user/login"; // Replace with your actual login endpoint
        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                response -> {
                    // Handle successful login response from the server

                    //fazer parse json, e ver se tem propiedade, sucesso e token e guardar token na shared preference
                    if (response.contains("Success")) {
                        // If login is successful, navigate to MenuActivity
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    } else {
                        // Handle unsuccessful login (invalid credentials, etc.)
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                }



        ) {

            // Override getParams method to pass username and password
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                // Add other necessary parameters

                return params;
            }

             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json"); // Example: Sending JSON data
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }



        };

        // Add the request to the RequestQueue
        Singleton.getInstance(this).volleyQueue.add(stringRequest);

        /*//TODO: Verificar se o user existe, através da API
        if (true)
        {
            Intent activity = new Intent(this, MenuActivity.class);
            startActivity(activity);
        }*/
    }

    public void onClickButtonLogin(View view) {
        Intent activity = new Intent(this, MenuActivity.class);
        startActivity(activity);
    }

    public void onClickAppConfig(View view) {
        //TODO: Abrir menu de configurações!
    }

    public void onClickSignUp(View view) {
        Intent activity = new Intent(this, SignUpActivity.class);
        startActivity(activity);
    }
}