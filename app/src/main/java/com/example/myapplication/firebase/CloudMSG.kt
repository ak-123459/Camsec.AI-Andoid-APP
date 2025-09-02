package com.example.myapplication.firebase



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.HomeActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.local.db.DatabaseProvider
import com.example.myapplication.local.db.NotificationEntity
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.viewModels.NotificationViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import  com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // TODO: Send token to your server for storage
    }

    private val secSF = SecurePrefsManager


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        if (secSF.getParentCode(this) != null) {


                // Check if notifications are enabled
                val enabled = secSF.getNotificationsEnabled(applicationContext)
                if (!enabled) return  // Stop processing if disabled



               val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title ?: "Attendance"
               val body  = remoteMessage.data["body"]  ?: remoteMessage.notification?.body  ?: "Marked successfully"


                // Save in Room DB
                val db = DatabaseProvider.getDatabase(applicationContext)
                val notification = NotificationEntity(
                    title = title,
                    body = body,
                    timestamp = System.currentTimeMillis()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    db.notificationDao().insert(notification)
                }

               val notificationId = System.currentTimeMillis().toInt()


                // Intent to open your target activity
                val intent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("screen", "notification") // pass data if needed
                }

                // PendingIntent to launch when notification is clicked
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


                // Create Notification
                val channelId = "my_channel_id"
                val builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.camseclogo) // your icon
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

                // For Android 8.0+, create channel
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        notificationId.toString(),
                        "My Channel",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    channel.enableLights(true)
                    channel.enableVibration(true)
                    manager.createNotificationChannel(channel)
                }

                // Show notification
                manager.notify(1, builder.build())
            }

        }

    }



fun getFCMToken(onTokenReceived: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }

        val token = task.result
        Log.d("FCM", "Token: $token")
        onTokenReceived(token)
    }
}


