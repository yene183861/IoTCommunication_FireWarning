package vn.hust.soict.project.iotcommunication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
import vn.hust.soict.project.iotcommunication.model.User;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    MqttAndroidClient client;
    String topic = "iot_communication";

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
        createChannelNotification();
//        connectToMQTTBroker();
        Log.e("id client", DataLocalManager.getClientId());
//        if(!DataLocalManager.getClientId().isEmpty()){
//            connectToMQTTBroker();
//        }
        //connectToMQTTBroker();
    }

    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
//    private void connectToMQTTBroker(){
//        String clientId = DataLocalManager.getClientId();
//        client =
//                new MqttAndroidClient(this.getApplicationContext(), "tcp://27.72.98.181:3307",
//                        clientId);
//        String topic = "iot_communication";
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//
//        try {
//            //IMqttToken token = client.connect(options);
//            options.setUserName("USERNAME");
//            options.setPassword("PASSWORD".toCharArray());
//            IMqttToken token = client.connect(options);
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.e("connect mqtt", "onSuccess");
//                    subscribe();
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.e("connect mqtt", "onFailure" + exception);
//
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//    private void publish(){
//        String payload = "";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }
    private void subscribe(){
        String topic = "iot_communication";
        int qos = 0;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.e("receive message", "success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.e("receive message", "failed: " + exception);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
