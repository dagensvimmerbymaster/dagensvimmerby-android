package se.dagensvimmerby.CloudMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import crm.agile.agilecrm.R;
import se.dagensvimmerby.Home;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                String pushData = remoteMessage.getData().get("data");
                JSONObject jsonObject = new JSONObject(pushData);
                String messageBody = jsonObject.getString("alert");
                sendNotification(this, getApplicationName(this), messageBody);
            } catch (Exception e) {
            }
        }
    }

    public void sendNotification(Context context, String title,  String messageBody) {

        wakeScreenIfNeeded(context);

        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "id-"+title;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_notification)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        notificationManager.notify(messageBody.hashCode() /* ID of notification */, notificationBuilder.build());

    }

    private void wakeScreenIfNeeded(Context context) {
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = isScreenOn(pm);

        // Log.d(TAG, "IsScreenOn: " + isScreenOn);

        if(!isScreenOn)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(1000);
        }
    }

    private boolean isScreenOn(PowerManager pm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            return pm.isInteractive();
        } else {
            return pm.isScreenOn();
        }
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

}

