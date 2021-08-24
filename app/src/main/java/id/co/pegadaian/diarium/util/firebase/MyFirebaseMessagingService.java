package id.co.pegadaian.diarium.util.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.temp.MainActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]


        //"Title","Message","NotyType",   "hotelStatus"


// buat dari console
//        String title = "";
//        if (remoteMessage.getNotification().getTitle() != null){
//            title = remoteMessage.getNotification().getTitle();
//        }
//
//        String message = "";
//        if (remoteMessage.getNotification().getBody() != null){
//            message = remoteMessage.getNotification().getBody();
//        }

//        String title = "";
//        String message = "";
//
//        if (remoteMessage.getNotification() != null){
//            if (remoteMessage.getNotification().getTitle() != null){
//                title = remoteMessage.getNotification().getTitle();
//            }
//
//            if (remoteMessage.getNotification().getBody() != null){
//                message = remoteMessage.getNotification().getBody();
//            }
//        }else{
//            if (remoteMessage.getData().get("title") != null){
//                title = remoteMessage.getData().get("title");
//            }
//            if (remoteMessage.getData().get("message") != null){
//                message = remoteMessage.getData().get("message");
//            }
//        }
//
//
//
//
//        System.out.println("tes isi");
//        System.out.println(remoteMessage.getData().toString());
//
//      //  Log.e("notification","recieved");
//
//        sendNotification(title, message);
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {

        Intent intent = new Intent(this, HomeActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

       // int badgeCount = 1;
        ShortcutBadger.applyCount(this, 1);

//        Intent in = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//        in.putExtra("badge_count", 1);
//        in.putExtra("badge_count_package_name", "com.sigma.mforce.telkomlandmarktower");
//        sendBroadcast(in);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_aja)
                //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
               //         R.mipmap.ic_launcher))
                //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
               //                  R.drawable.logo_tlt2))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(getRequestCode(), notificationBuilder.build());
    }

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }

}