package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName, editTextSurname, editTextNIF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initcialização te textfieds ou edittextfields
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextName = findViewById(R.id.editTextText);
        editTextSurname = findViewById(R.id.editTextText2);
        editTextNIF = findViewById(R.id.editTextText3);

    }

    public void onClickSignUp(View view) {

        //ir buscar texto inserido
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String nif = editTextNIF.getText().toString();

        // Volley POST request to your API endpoint with this user data
        // localhost refers to the device itself, not the server where your backend is hosted. You should use the server's IP address or domain name instead of localhost.
        //Aqui o meu localhost é o meu ip, rede onde estou ligado, onde o apache é hosted
        String signUpUrl = "http://192.168.137.235:8888/HoraDaPapa/backend/web/api/user/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, signUpUrl,
                response -> {
                    // Handle successful registration
                    // Maybe navigate to the login screen
                    Toast.makeText(this, "Signed up!", Toast.LENGTH_SHORT).show();
                    Intent activity = new Intent(this, MainActivity.class);
                    startActivity(activity);
                },
                error -> {
                    // Handle error
                    Log.e("SignUpError", error.toString());

                }) {
            @Override
            // Creating a map to send as parameters in the POST request
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", name);
                params.put("email", email);
                params.put("password", password);
                // Add other user data here

                return params;
            }
        };

        // Add the request to the RequestQueue
        Singleton.getInstance(this).volleyQueue.add(stringRequest);
    }
    }