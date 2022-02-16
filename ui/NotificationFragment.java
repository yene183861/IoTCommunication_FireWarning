package vn.hust.soict.project.iotcommunication.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.adapter.DeviceListAdapter;
import vn.hust.soict.project.iotcommunication.adapter.NotificationListAdapter;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
import vn.hust.soict.project.iotcommunication.model.Device;
import vn.hust.soict.project.iotcommunication.model.Home;
import vn.hust.soict.project.iotcommunication.model.Notification;
import vn.hust.soict.project.iotcommunication.viewmodel.DeviceListViewModel;
import vn.hust.soict.project.iotcommunication.viewmodel.NotificationListViewModel;

public class NotificationFragment extends Fragment {
//    private MqttAndroidClient client;
//    private String topic = DataLocalManager.getClientId() + "rece";
    private Context mContext;
    private RecyclerView rcvNotification;
    private NotificationListViewModel listViewModel;
    private List<Notification> mList;
    private NotificationListAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_notification, container, false);
       // connectMQTT();
//        Bundle bundle = this.getArguments();
//        Notification notification = (Notification) bundle.getSerializable("notification");
        rcvNotification = view.findViewById(R.id.rcvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rcvNotification.setLayoutManager(linearLayoutManager);
        adapter = new NotificationListAdapter(mContext, mList);
        rcvNotification.setAdapter(adapter);
        //mList.add(notification);
        listViewModel = new ViewModelProvider(this).get(NotificationListViewModel.class);
        listViewModel.getNotificationsListObserver().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> list) {
                if (list == null) {
                    rcvNotification.setVisibility(View.GONE);
//                    tvNoNotificationList.setVisibility(View.VISIBLE);
                } else {
                    mList = list;
                    adapter.setNotificationList(list);
                    //tvNoNotificationList.setVisibility(View.GONE);
                    rcvNotification.setVisibility(View.VISIBLE);
                }
            }
        });
        listViewModel.getNotificationList();
        //mList.add(notification);

        return view;

    }

//    @Override
//    public void onDestroy() {
//        disconnectMQTT();
//        Log.e("onDestroy", "onDestroy");
//        super.onDestroy();
//    }

//    public void connectMQTT() {
//        String clientId = MqttClient.generateClientId();
//        client =
//                new MqttAndroidClient(mContext, "tcp://27.72.98.181:3307",
//                        clientId);
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//        options.setUserName("USERNAME");
//        options.setPassword("USERNAME".toCharArray());
//
//        try {
//            IMqttToken token = client.connect(options);
//            token.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // We are connected
//                    Log.e("connect mqtt", "onSuccess");
//                    subscribe(topic);
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
//
//    public void subscribe(String topicName) {
//        try {
//            Log.d("mqtt", "topic name: " + topicName);
//            Log.d("mqtt", "status connect: " + client.isConnected());
//            if (client.isConnected()) {
//                client.subscribe(topicName, 0);
//                client.setCallback(new MqttCallback() {
//                    @Override
//                    public void connectionLost(Throwable cause) {
//                        Log.e("subscribe mqtt: ", "Connection was lost!");
//                    }
//
//                    @Override
//                    public void messageArrived(String topic, MqttMessage message) throws Exception {
//                        Log.e("subscribe mqtt: ", "message>>" + new String(message.getPayload()));
//                        Log.e("subscribe mqtt: ", "topic>>" + topic);
//                        Log.e("message", new String(message.getPayload()));
//                       // parseMqttMessage(new String(message.getPayload()));
//                        adapter.setNotificationList(mList);
//                    }
//
//                    @Override
//                    public void deliveryComplete(IMqttDeliveryToken token) {
//                        Log.e("subscribe mqtt: ", "Delivery Complete!");
//                    }
//                });
//            }
//        } catch (Exception e) {
//            Log.d("subscribe mqtt: ", "Error :" + e);
//        }
//    }
//
//    private void parseMqttMessage(String s) {
//        Log.e("message receive", s);
//    }
//
//    private void disconnectMQTT() {
//        if (client.isConnected()) {
//            try {
//                IMqttToken disconToken = client.disconnect();
//                disconToken.setActionCallback(new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        // we are now successfully disconnected
//                        Log.e("mqtt", "disconnect success");
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken,
//                                          Throwable exception) {
//                        Log.e("mqtt", "disconnect failed " + exception);
//                        // something went wrong, but probably we are disconnected anyway
//                    }
//                });
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
