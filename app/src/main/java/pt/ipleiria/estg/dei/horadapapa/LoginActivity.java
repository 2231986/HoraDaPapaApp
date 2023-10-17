package pt.ipleiria.estg.dei.horadapapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.API;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editText_username);
        etPassword = findViewById(R.id.editText_password);

        Singleton.getInstance(this);
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

        //Comunicação com a API (ongoing)
        //API.userLogin(this, username, password);
        //finish();

        //TODO: Verificar se o user existe, através da API
        if (username == "admin" && password == "admin")
        {
            Intent activity = new Intent(this, MenuActivity.class);
            startActivity(activity);
        }
    }
}