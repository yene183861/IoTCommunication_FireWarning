package vn.hust.soict.project.iotcommunication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
import vn.hust.soict.project.iotcommunication.model.Notification;

//implements NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FrameLayout contentFrame;
    private MqttAndroidClient client;
    private String topic = DataLocalManager.getClientId() + "rece";
    SwipeListener swipeListener;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_NOTIFICATION = 1;
    private static final int FRAGMENT_MY_PROFILE = 2;
    private int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectMQTT();
        //DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationView = findViewById(R.id.bottomNav);
        contentFrame = findViewById(R.id.contentFrame);
        replaceFragment(new ManageHomeFragment());
        getSupportActionBar().setTitle("Manage Home");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize swipe listener
        swipeListener = new SwipeListener(contentFrame);
        //initUi();

        // showInfoUser();
        mNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    if (currentFragment != FRAGMENT_HOME) {
                        replaceFragment(new ManageHomeFragment());
                        currentFragment = FRAGMENT_HOME;
                        getSupportActionBar().setTitle("Manage Home");
                    }
                    break;
                case R.id.action_notification:
                    if (currentFragment != FRAGMENT_NOTIFICATION) {
                        replaceFragment(new NotificationFragment());
                        currentFragment = FRAGMENT_NOTIFICATION;
                    }
                    getSupportActionBar().setTitle("Notification");
                    break;
                case R.id.action_profile:
                    if (currentFragment != FRAGMENT_MY_PROFILE) {
                        replaceFragment(new MyProfileFragment());
                        currentFragment = FRAGMENT_MY_PROFILE;
                        getSupportActionBar().setTitle("My profile");
                        break;
                    }
            }
            return true;
        });
    }



    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, fragment);
        transaction.commit();
    }

    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(FrameLayout view) {
            //Initialize threshold value
            int threshold = 100;
            int velocityThreshold = 100;

            //initialize simple gesture listener
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();
                            try {
                                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                                    if (Math.abs(xDiff) > threshold
                                            && Math.abs(velocityX) > velocityThreshold) {
                                        if (xDiff > 0) {
                                            Log.e("GestureDetector", "swipe right");
                                            //when swiped right
                                        } else {
                                            Log.e("GestureDetector", "swipe left");
                                            //when swiped left
                                        }
                                        return true;
                                    }
                                } else {
                                    //when y > x swipe down or up
//                                    if (Math.abs(xDiff) > Math.abs(yDiff)) {
//                                        if (Math.abs(xDiff) > threshold
//                                                && Math.abs(velocityX) > velocityThreshold) {
//                                            if (xDiff > 0) {
//                                                Log.e("GestureDetector", "swipe right");
//                                                //when swiped right
//                                            } else {
//                                                Log.e("GestureDetector", "swipe left");
//                                                //when swiped left
//                                            }
                                            return false;
                                        //}

                               // }
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            //initialize gesture detector
            gestureDetector = new GestureDetector(listener);
            //set listener on view
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //return gesture event
            return gestureDetector.onTouchEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        disconnectMQTT();
        super.onDestroy();
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
//        return true;
//    }
    //    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    private void showInfoUser() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
//        String name = user.getDisplayName();
//        String email = user.getEmail();
//        Uri photoUrl = user.getPhotoUrl();
//        Log.e("user", email);
//        if(name != null){
//            txtName.setText(name);
//            txtName.setVisibility(View.VISIBLE);
//        }
//        txtEmail.setText(email);
//        Glide.with(this).load(photoUrl).error(R.drawable.default_avatar).into(imgAvatar);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void connectMQTT() {
        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this, "tcp://27.72.98.181:3307",
                        clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("USERNAME");
        options.setPassword("USERNAME".toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.e("connect mqtt", "onSuccess");
                    subscribe(topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.e("connect mqtt", "onFailure" + exception);

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topicName) {
        try {
            Log.d("mqtt", "topic name: " + topicName);
            Log.d("mqtt", "status connect: " + client.isConnected());
            if (client.isConnected()) {
                client.subscribe(topicName, 0);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.e("subscribe mqtt: ", "Connection was lost!");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        //Log.e("subscribe mqtt: ", "message>>" + new String(message.getPayload()));
                        Log.e("subscribe mqtt: ", "topic>>" + topic);
                        //Log.e("message", new String(message.getPayload()));
                        //Notification notifi = parseMqttMessage(new String(message.getPayload()));
                        //Log.e("mess",notifi.toString());
                        JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                        //showAlert(notifi.getContent());
                        Gson gson=new Gson();
                        Notification notification = gson.fromJson(jsonObject.toString(), Notification.class);
                        Log.e("mess", notification.toString());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.e("subscribe mqtt: ", "Delivery Complete!");
                    }
                });
            }
        } catch (Exception e) {
            Log.d("subscribe mqtt: ", "Error :" + e);
        }
    }


    private void disconnectMQTT() {
        if (client.isConnected()) {
            try {
                IMqttToken disconToken = client.disconnect();
                disconToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // we are now successfully disconnected
                        Log.e("mqtt", "disconnect success");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        Log.e("mqtt", "disconnect failed " + exception);
                        // something went wrong, but probably we are disconnected anyway
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
    private void showAlert(Notification notification){
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        NotificationFragment fragment = new NotificationFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("notification", notification);
        fragment.setArguments(bundle);
//        transaction.replace(R.id.contentFrame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//        Bundle bundle = new Bundle();
//        String str = "test";
//        bundle.putString("test", str);
//        fragment.setArguments(bundle);
        replaceFragment(fragment);

    }

}