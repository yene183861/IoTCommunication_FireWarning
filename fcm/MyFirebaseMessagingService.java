package vn.hust.soict.project.iotcommunication.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import vn.hust.soict.project.iotcommunication.MainActivity;
import vn.hust.soict.project.iotcommunication.MyApplication;
import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Notification Message
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if (notification == null) {
//            return;
//        }
//        String title = notification.getTitle();
//        String message = notification.getBody();
//        sendNotification(title, message);

        //Data message
        Map<String, String> stringMap = remoteMessage.getData();
        String title = stringMap.get("user_name");
        String body = stringMap.get("description");
        sendNotification(title, body);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("FCM", s);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(s);
        saveFCMTokenToDataLocal(s);
    }

    private void saveFCMTokenToDataLocal(String s) {
//        DataLocalManager.setTokenFCM(s);
    }

//    private void sendRegistrationToServer(String s) {
//    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.ic_ring)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1, notification);
        }
    }
}
