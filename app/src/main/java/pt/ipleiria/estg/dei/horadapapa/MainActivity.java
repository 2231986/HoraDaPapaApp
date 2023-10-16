package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButtonLogin(View view) {
        Intent activity = new Intent(this, LoginActivity.class);
        startActivity(activity);
    }

    public void onClickAppConfig(View view) {
        //TODO: Abrir menu de configurações!
    }
}