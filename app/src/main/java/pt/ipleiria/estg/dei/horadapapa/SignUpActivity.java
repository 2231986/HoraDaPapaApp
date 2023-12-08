package pt.ipleiria.estg.dei.horadapapa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.models.User;

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
        String username = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //TODO: acabar de implementar outros campos
        String surname = editTextSurname.getText().toString();
        String nif = editTextNIF.getText().toString();

        User user = new User(username, password);
        user.setEmail(email);

        Singleton.getInstance(this).requestUserRegister(this, user);
    }
}