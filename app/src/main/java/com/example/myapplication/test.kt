import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.local.db.NotificationEntity
import com.example.myapplication.viewModels.NotificationViewModel

// Define some colors for the UI
val LightGray = Color(0xFFF0F0F0)
val DarkPurple = Color(0xFF5A4D8D)
val LightPurple = Color(0xFF8B83B0)
val LightGreen = Color(0xFFD6F5D6)
val DarkGreen = Color(0xFF4CAF50)
val LightYellow = Color(0xFFFFFBE0)
val DarkYellow = Color(0xFFE9C54B)
val LightBlue = Color(0xFFD3E6FF)
val DarkBlue = Color(0xFF4285F4)

// Data class to represent a notification item
data class NotificationItem(
    val iconResId: Int,
    val title: String,
    val subtitle: String,
    val time: String,
    val iconBackgroundColor: Color,
    val iconContentColor: Color
)

@Composable
fun MostRecentNotif(viewModel: NotificationViewModel= viewModel()) {

    val notifications by viewModel.notifications.collectAsState(initial = listOf())
    viewModel.clearNotificationStatus()

    LaunchedEffect(Unit) {
        viewModel.notifications.collect { list ->
            list.forEach { notif ->
                Log.d("RoomDebug", "Notification: $notif")
            }
        }
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {


        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "See All",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))



        if (notifications.isEmpty()) {
            // ✅ Show empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notifications available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }


        }  else {

            notifications.forEach { item ->
                NotificationItemRow(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
        }


//        // List of notification items
//        val notificationItems = listOf(
//            NotificationItem(
//                // Replace with your actual icon resource, e.g., R.drawable.ic_check_circle
//                iconResId = R.drawable.ic_check_circle,
//                title = "Order placed successfully!",
//                subtitle = "Your order #12345 has been placed successfully. You can track its status in the app.",
//                time = "Now",
//                iconBackgroundColor = LightGreen,
//                iconContentColor = DarkGreen
//            ),
//            NotificationItem(
//                // Replace with your actual icon resource, e.g., R.drawable.ic_promotion
//                iconResId = R.drawable.ic_promotion,
//                title = "New promotion available",
//                subtitle = "Check out our new promotions and get a discount on your next purchase!",
//                time = "1h ago",
//                iconBackgroundColor = LightBlue,
//                iconContentColor = DarkBlue
//            ),
//            NotificationItem(
//                // Replace with your actual icon resource, e.g., R.drawable.ic_payment
//                iconResId = R.drawable.ic_payment,
//                title = "Payment successful",
//                subtitle = "Your payment for order #98765 has been successfully processed.",
//                time = "3h ago",
//                iconBackgroundColor = LightYellow,
//                iconContentColor = DarkYellow
//            )
//        )


    }


@Composable
fun NotificationItemRow(item: NotificationEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camseclogo),
                    contentDescription = null,

                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    item.title?.let {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = item.timestamp.toString(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                item.body?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NotificationsScreenPreview() {
//    // You'll need to define dummy resources or use a tool that can provide them for the preview
//    // For example, you can create a R.drawable.ic_check_circle in your project
//    NotificationsScreen()
//}
