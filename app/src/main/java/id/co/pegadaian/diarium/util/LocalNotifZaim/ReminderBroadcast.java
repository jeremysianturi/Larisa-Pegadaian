package id.co.pegadaian.diarium.util.LocalNotifZaim;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.HomeActivity;

public class ReminderBroadcast extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    Intent intent1 = new Intent(context, HomeActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);


    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"diariumpagi")
            .setContentTitle("Diarium Supper Apps ")
            .setContentText("Don't forget to Check In/Check Out")
            .setSmallIcon(R.drawable.ic_logo_diarium)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_logo_diarium))
            .setAutoCancel(true)
            .addAction(R.drawable.ic_logo_diarium,"Open",pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

    notificationManager.notify(200,builder.build());
  }
}
