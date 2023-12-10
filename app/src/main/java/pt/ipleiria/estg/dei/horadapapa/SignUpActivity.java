package pt.ipleiria.estg.dei.horadapapa;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.models.User;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etName, etSurname, etNIF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.et_Username);
        etEmail = findViewById(R.id.et_Email);
        etPassword = findViewById(R.id.et_Password);
        etName = findViewById(R.id.et_Name);
        etSurname = findViewById(R.id.et_Surname);
        etNIF = findViewById(R.id.et_NIF);
    }

    public void onClickSignUp(View view) {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String nif = etNIF.getText().toString();

        //Guard clauses
        if (!ProjectHelper.isUsernameValid(username)) {
            etUsername.setError("Invalid Username");
            BetterToast(getBaseContext(),"Invalid Username");
            return;
        }

        if (!ProjectHelper.isEmailValid(email)) {
            etEmail.setError("Invalid Email");
            BetterToast(getBaseContext(),"Invalid Email");
            return;
        }

        if (!ProjectHelper.isPasswordValid(password)) {
            etPassword.setError("Invalid Password");
            BetterToast(getBaseContext(),"Invalid Password");
            return;
        }

        if (!nif.isEmpty() && !ProjectHelper.isNifValid(nif)) {
            etNIF.setError("Invalid NIF");
            BetterToast(getBaseContext(),"Invalid NIF");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setNif(nif);

        Singleton.getInstance(this).requestUserRegister(this, user);
    }
}