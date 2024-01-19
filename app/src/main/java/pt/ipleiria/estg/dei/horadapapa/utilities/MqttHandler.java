package pt.ipleiria.estg.dei.horadapapa.utilities;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHandler {

    private static final String TAG = "MQTT: ";

    public static String MQTT_BROKER_URI = "tcp://10.0.2.2:1883";
    public String ClientID = "0";

    private MqttClient mqttClient = null;

    public MqttHandler(Context context, String clientID) {
        this.ClientID = clientID;

        AppPreferences appPreferences = new AppPreferences(context);
        String mqttHost = appPreferences.getMqttIP();

        if (mqttHost != null && !mqttHost.isEmpty()) {
            MQTT_BROKER_URI = mqttHost;
        }

        try {
            mqttClient = new MqttClient(MQTT_BROKER_URI, this.ClientID, null);
            if (mqttClient != null) {
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttConnectOptions.setCleanSession(true);

                mqttClient.connect(mqttConnectOptions);
                BetterToast(context, TAG + "Successfully connected to the broker");

                mqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        BetterToast(context, TAG + "Connection lost");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        String payload = new String(message.getPayload());
                        new Handler(Looper.getMainLooper()).post(() -> BetterToast(context, payload));
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        BetterToast(context, TAG + "Message delivery complete");
                    }
                });
            }
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    //Para testes no cmd do server: mosquitto_pub -h localhost -t topic_1 -m "Hello, Mosquitto!"
    public void subscribeToTopic(Context context, String topic) {
        try {
            mqttClient.subscribe(topic, 0);
            Log.d(TAG, "Successfully subscribed to topic " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishToTopic(String topic, String message) {
        try {
            mqttClient.publish(topic, new MqttMessage(message.getBytes()));
            Log.d(TAG, "Successfully published message to topic " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
            Log.d(TAG, "Successfully disconnected from the broker");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
