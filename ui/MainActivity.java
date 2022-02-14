package vn.hust.soict.project.iotcommunication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.model.User;
import vn.hust.soict.project.iotcommunication.ui.ChangePasswordFragment;
import vn.hust.soict.project.iotcommunication.ui.HomeFragment;
import vn.hust.soict.project.iotcommunication.ui.ManageHomeFragment;
import vn.hust.soict.project.iotcommunication.ui.MyProfileFragment;
import vn.hust.soict.project.iotcommunication.ui.NotificationFragment;

//implements NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FrameLayout contentFrame;
    private ViewPager2 mViewPager2;
    SwipeListener swipeListener;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_NOTIFICATION = 1;
    private static final int FRAGMENT_MY_PROFILE = 2;
//    private DrawerLayout drawerLayout;
//    private ImageView imgAvatar;
//    private TextView txtName, txtEmail;
//    private NavigationView navigationView;
//    private static final int FRAGMENT_HOME = 0;
//    private static final int FRAGMENT_MANAGE_DEVICE = 1;
//    private static final int FRAGMENT_NOTIFICATION = 2;
//    private static final int FRAGMENT_MY_PROFILE = 3;
//    private static final int FRAGMENT_CHANGE_PASSWORD = 4;
//    private Messenger messenger;
//    private boolean isServiceConnected;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder iBinder) {
//messenger = new Messenger(iBinder);
//isServiceConnected = true;
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
//    String clientId = User.getMacAddr();
//    MqttAndroidClient client =
//            new MqttAndroidClient(this, "tcp://broker.hivemq.com:1883",
//                    clientId);
//    String topic = "iot_communication";
//    String token;

    private int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DRAWER
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = getIntent();
//        token = intent.getStringExtra("token");
//        Log.e("token", token);
//        Intent intent = new Intent(this, MyService.class);
//        bindService(intent)
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

//    private void initUi() {
//        navigationView = findViewById(R.id.navigationView);
//        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
//        txtName = navigationView.getHeaderView(0).findViewById(R.id.txtName);
//        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
//    }

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
                                    return false;
                                    //when y > x swipe down or up
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
}