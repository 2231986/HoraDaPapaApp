package pt.ipleiria.estg.dei.horadapapa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.MqttHandler;

public class APIConfActivity extends AppCompatActivity {

    private EditText et_ApiHost, et_MqttHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        AppPreferences appPreferences = new AppPreferences(this);

        et_ApiHost = findViewById(R.id.et_ApiHost);
        String apiHost = appPreferences.getApiIP();

        if (apiHost != null && !apiHost.isEmpty()){
            et_ApiHost.setText(apiHost);
        }else {
            et_ApiHost.setText(Singleton.ApiHost);
        }

        et_MqttHost = findViewById(R.id.et_MqttHost);
        String mqttHost = appPreferences.getMqttIP();

        if (mqttHost != null && !mqttHost.isEmpty()){
            et_MqttHost.setText(mqttHost);
        }else {
            et_MqttHost.setText(MqttHandler.MQTT_BROKER_URI);
        }
    }

    public void appConfigSave(View view) {

        AppPreferences appPreferences = new AppPreferences(this);

        String apiHost = et_ApiHost.getText().toString();

        if (apiHost != null && !apiHost.isEmpty()){
            appPreferences.setApiIP(apiHost);
        }

        String mqttHost = et_MqttHost.getText().toString();

        if (mqttHost != null && !mqttHost.isEmpty()){
            appPreferences.setMqttIP(mqttHost);
        }

        finish();
    }
}