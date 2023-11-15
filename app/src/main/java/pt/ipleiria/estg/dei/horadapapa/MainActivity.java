package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;


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

        //TODO: Verificar se o user existe, através da API
        if (true)
        {
            Intent activity = new Intent(this, MenuActivity.class);
            startActivity(activity);
        }
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