package com.example.myapplication.screens.home.notifications

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewModels.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*



@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(viewModel: NotificationViewModel) {

    val notifications by viewModel.notifications.collectAsState(initial = listOf())
    val selectedNotificationIds by viewModel.selectedNotificationIds
    val isSelectionMode = selectedNotificationIds.isNotEmpty()
    viewModel.clearNotificationStatus()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Scaffold(
            topBar = {

                    if (isSelectionMode) {
                        SelectionTopAppBar(
                            selectedCount = selectedNotificationIds.size,
                            onDeleteSelected = { viewModel.deleteSelectedNotifications() },
                            onCloseSelection = { viewModel.selectedNotificationIds.value = emptySet() }
                        )
                    } else {

                TopAppBar(
                    title = {
                        Text(
                            "Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1976D2),
                        titleContentColor = Color.White
                    )
                )
            }


            }
        ) { padding ->

            if (notifications.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = padding,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8F9FA))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    items(
                        items = notifications,
                        key = { notification -> notification.id }
                    ) { notification ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.deleteNotification(notification.id)
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                    Color.Red.copy(alpha = 0.9f)
                                } else {
                                    Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            },
                            content = {

                                val isSelected = selectedNotificationIds.contains(notification.id)

                                NotificationCard(
                                    notification = NotificationItem(
                                        id = notification.id,
                                        title = notification.title ?: "",
                                        message = notification.body ?: "",
                                        time = notification.timestamp,
                                        isRead = notification.isRead
                                    ),isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    onClick = { viewModel.markAsRead(notification.id) }
                                , onLongClick =  {
                                        if (!isSelectionMode) {
                                            viewModel.toggleSelectAllNotifications()
                                        }
                                    })
                            }
                        )
                    }
                }
            }
        }
    }
}

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: Long,
    val isRead: Boolean
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationCard(notification: NotificationItem,isSelected: Boolean,
                     isSelectionMode: Boolean, onClick: () -> Unit, onLongClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeString = dateFormat.format(Date(notification.time))

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> Color(0xFFB3E5FC) // Light blue for selected items
                notification.isRead -> Color.White
                else -> Color(0xFFE3F2FD) // Unread color
            })
            ,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {

            // Checkbox for multi-selection
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() }, // Re-use the onClick handler to toggle selection
                    modifier = Modifier.size(24.dp),
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1976D2))
                )
                Spacer(modifier = Modifier.width(10.dp))
            }




            // Notification Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Notification Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Time + Unread Dot
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeString,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (!notification.isRead && !isSelectionMode) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopAppBar(selectedCount: Int, onDeleteSelected: () -> Unit, onCloseSelection: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "$selectedCount selected",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseSelection) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Selection"
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteSelected) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Selected"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1976D2),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

