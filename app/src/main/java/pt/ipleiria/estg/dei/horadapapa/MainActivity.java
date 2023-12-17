package pt.ipleiria.estg.dei.horadapapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.models.User;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.editText_username);
        etPassword = findViewById(R.id.editText_password);
    }

    public void onClickButtonLogin(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!ProjectHelper.isUsernameValid(username)) {
            etUsername.setError("Username inválido!");
            ProjectHelper.BetterToast(this, "Username inválido!");
            return;
        }

        if (!ProjectHelper.isPasswordValid(password)) {
            etPassword.setError("Password inválida!");
            ProjectHelper.BetterToast(this, "Password inválida!");
            return;
        }

        User user = new User(username, password);

        Singleton.getInstance(this).requestUserLogin(this, user);
    }

    public void onClickAppConfig(View view) { //Abre a Activity para configurar a aplicação
        Intent intent = new Intent(this, AppConfingActivity.class);
        startActivity(intent);
/*<<<<<<< Updated upstream
=======
        //finish();
>>>>>>> Stashed changes*/
    }

    public void onClickSignUpActivity(View view) { //Abre a Activity para registar o cliente
        Intent activity = new Intent(this, SignUpActivity.class);
        startActivity(activity);
    }
}