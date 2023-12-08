package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;

public class APIConfActivity extends AppCompatActivity {

    private EditText et_ApiHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        et_ApiHost = findViewById(R.id.et_ApiHost);

        AppPreferences appPreferences = new AppPreferences(this);
        String apiHost = appPreferences.getApiIP();

        if (apiHost != null && !apiHost.isEmpty()){
            et_ApiHost.setText(apiHost);
        }else {
            et_ApiHost.setText(Singleton.ApiHost);
        }
    }

    public void appConfigSave(View view) {
        String apiHost = et_ApiHost.getText().toString();

        if (apiHost != null && !apiHost.isEmpty()){
            AppPreferences appPreferences = new AppPreferences(this);
            appPreferences.setApiIP(apiHost);
        }

        finish();
    }
}