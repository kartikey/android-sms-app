package edu.uw.kartikey.yama;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by kartikey on 2/1/2016.
 */
public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("RECEIVER", intent.toString());

        SmsMessage[] messages = getMessagesFromIntent(intent);

        for(SmsMessage msg : messages){
            Log.v("RECEIVER", msg.getOriginatingAddress() + ": "+msg.getMessageBody());
//            Toast t = Toast.makeText(context, msg.getOriginatingAddress() + ": "+msg.getMessageBody(), Toast.LENGTH_LONG);
//            t.show();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

            Boolean autoReply = sharedPref.getBoolean("auto_reply_key", false);
            String autoMessage = sharedPref.getString("message_key", "");

            if(autoReply) {
                Log.v("RECEIVER", "autoreply: "+autoReply.toString() + "send text "+autoMessage);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(msg.getOriginatingAddress(), null, autoMessage, null, null);
            }



            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle("Message from " + msg.getOriginatingAddress())
                    .setContentText(msg.getMessageBody())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0,mBuilder.build());

        }
    }
}
