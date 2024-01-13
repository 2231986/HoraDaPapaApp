package pt.ipleiria.estg.dei.horadapapa.utilities;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHandler {

    private static final String TAG = "MQTT: ";

    //TODO: adicionar o ip correto
    public static String MQTT_BROKER_URI = "tcp://my_mosquitto_broker_ip:1883";
    private String ClientID;

    private final MqttAndroidClient mqttAndroidClient;

    public MqttHandler(Context context, String clientID) {

        this.ClientID = clientID;

        AppPreferences appPreferences = new AppPreferences(context);
        String mqttHost = appPreferences.getMqttIP();

        if (mqttHost != null && !mqttHost.isEmpty())
        {
            this.MQTT_BROKER_URI = mqttHost;
        }

        mqttAndroidClient = new MqttAndroidClient(context, MQTT_BROKER_URI, this.ClientID);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    BetterToast(context, TAG + "Successfully connected to the broker");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    BetterToast(context, TAG + "Failed to connect to the broker");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                BetterToast(context, TAG + "Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                BetterToast(context, TAG + "Received message on topic " + topic + ": " + payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                BetterToast(context, TAG + "Message delivery complete");
            }
        });
    }

    public void subscribeToTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Successfully subscribed to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failed to subscribe to topic " + topic, exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishToTopic(String topic, String message) {
        try {
            mqttAndroidClient.publish(topic, message.getBytes(), 0, false, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Successfully published message to topic " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to publish message to topic " + topic, exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Successfully disconnected from the broker");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to disconnect from the broker", exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
