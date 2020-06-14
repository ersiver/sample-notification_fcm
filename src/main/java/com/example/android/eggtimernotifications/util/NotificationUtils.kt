package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// Extension function to send messages (it's like a class with build method)
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    /**
     * 1. Adding intent, which launches this activity and allows user to fo to the app
     * once the notification is clicked outside the app.
     */

    //1.1 Creating intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // 1.2 To make an intent work outside the app the PendingIntent is needed.
    // Flags specify the option to create a new PI or use existing one.
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    /**
     * 2. Customizing notification. There are plenty styles like NotificationCompat.BigTextStyle, InboxStyle, MediaStyle
     *
     */

    //2.1 We will use BigPictureStyle which is Expandable notif. that shows a large icon when expanded.
    //set bigLargeicon as null so the large icon goes away when notif. is expanded
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // 2.2 Adding actions, that allow the user to respond quickly.
    //This one will be used to set up a new alarm to post a new notification
    //after 60sec when the snooze button is tapped by the user.
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    /**
     * 3. Get an instance of NotificationCompat.Builder and build the notification
     */
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )
        //3.1 setting notification icon to represent the app, the title and the message
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        //3.2 Set content intent, so when click on notification, PendingIntent will be
        //trigerred opening the MainActivity.
        .setContentIntent(pendingIntent)

        //3.3 When the user taps on the notification, it dismisses and takes them to the app
        .setAutoCancel(true)

        //3.4 Add style to builder. Set the large icon, so the image will be displayed as a smaller icon
        //when notification is collapsed.
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)

        //3.5 Add snooze action. To add some action button pass a PendingIntent to the addAction()
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        //3.6 Set priority
        //to support devices running  on API<25 use this method.
        //For higher the impotance is set in NotificationChannel constructor in fragment
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    //3.7 call notify() passing a unique ID for the current notification instance and the Notification object
    notify(NOTIFICATION_ID, builder.build())
}


/**
 * 4. Cancel all notifications in order to clear the previous notifications when start a new timer.
 */

fun NotificationManager.cancelNotification() {
    cancelAll()
}


