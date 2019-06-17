package emschenn.csie.ncku.webparsetest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static android.app.Activity.RESULT_OK;

/**
 * Broadcast receiver for the alarm, which delivers the notification.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    public String notify = "";
    private cardData oldcard;
    private cardData newcard;

    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Deliver the notification.
        deliverNotification(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);
        notify = "";
        for(int i=0;i<MainActivity.myList1.size();i++){
            int website = -1;
            if(MainActivity.myList3.get(i).charAt(0) =='5'){
                website = 5;
            }
            if(MainActivity.myList3.get(i).charAt(0) =='A'){
                website = 4;
            }
            if(MainActivity.myList3.get(i).charAt(0) =='1'){
                website = 3;
            }
            if(MainActivity.myList3.get(i).charAt(0) =='V'){
                website = 2;
            }
            if(MainActivity.myList3.get(i).charAt(0) =='M'){
                website = 1;
            }
            //List1 = Episode
            //List2 = Title
            //List3 = web
            oldcard = new cardData(MainActivity.myList2.get(i),MainActivity.myList3.get(i),MainActivity.myList1.get(i));
            newcard = new cardData(MainActivity.myList2.get(i),MainActivity.myList3.get(i),crawl.crawl_func(website,MainActivity.myList2.get(i)));

            if(!MainActivity.myList1.get(i).equals(crawl.crawl_func(website,MainActivity.myList2.get(i)))){
                notify += MainActivity.myList2.get(i)+" : "+crawl.crawl_func(website,MainActivity.myList2.get(i))+"\n";
                MainActivity.cards.update(oldcard,newcard);
                System.out.println("NEq");
            }else{
                System.out.println("Eq");
                notify = "";
            }
        }
        //System.out.println(notify);
        if(!notify.equals("")){
            System.out.println(notify);
            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder
                    (context, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("更新囉")
                    .setContentText(notify)
                    .setContentIntent(contentPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            // Deliver the notification
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
            notify = "";
            System.out.println("GGG");
        }else {
            System.out.println("no change");
        }


    }


}
