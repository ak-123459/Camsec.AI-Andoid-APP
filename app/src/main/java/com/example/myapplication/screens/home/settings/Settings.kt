package com.example.myapplication.screens.settings

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.App.Companion.context
import com.example.myapplication.R
import com.example.myapplication.local.repository.cancelAllNotifications
import com.example.myapplication.screens.components.AppDetailsBottomSheet
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.utility.logoutUser




@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val authShardPref = SecurePrefsManager
    val context = LocalContext.current
    var bottomSheetState by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(700)) + slideInVertically(initialOffsetY = { -40 }),
                        exit = fadeOut()
                    ) {
                        Text("Settings", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                backgroundColor = Color.Black,
                elevation = 4.dp
            )
        },
        backgroundColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Animated Settings List
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(initialOffsetX = { -80 }),
                exit = fadeOut()
            ) {
                SettingsList(onAboutClick = { bottomSheetState = true }, authShardPref)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Fade-in Buttons
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { 60 }),
                exit = fadeOut()
            ) {
                ActionButtons(context, navController)
            }
        }
    }

    if (bottomSheetState) {
        AppDetailsBottomSheet(onDismiss = { bottomSheetState = false })
    }
}

@Composable
fun SettingsList(onAboutClick: () -> Unit,secSharedPreferences:
SecurePrefsManager) {
    val settings = listOf(
        SettingItem("Notifications", Icons.Default.Notifications),
        SettingItem("Privacy", Icons.Default.Lock),
        SettingItem("Account", Icons.Default.AccountCircle),
        SettingItem("About", ImageVector.vectorResource(R.drawable.about)),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        settings.forEachIndexed { index, setting ->
            // 🔹 Staggered card animations
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(400, delayMillis = index * 100)) +
                        slideInVertically(initialOffsetY = { 40 }),
                exit = fadeOut()
            ) {
                SettingItemCard(setting, onAboutClick,secSharedPreferences)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SettingItemCard(setting: SettingItem, onAboutClick: () -> Unit,secSharedPreferences: SecurePrefsManager) {
    var isToggled by remember { mutableStateOf(false) }

    var notificationsEnabled by remember {
        mutableStateOf(secSharedPreferences.getNotificationsEnabled(context))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 6.dp,
        backgroundColor = Color.Black,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = setting.icon,
                    contentDescription = setting.title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = setting.title,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            when (setting.title) {
                "Notifications" -> {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                            notificationsEnabled = enabled
                            isToggled = true
                            SecurePrefsManager.saveNotificationsEnabled(context, enabled)


                            if (!enabled) {
                                cancelAllNotifications(context)

                            }
                        },

                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF1E88E5)
                        )
                    )
                }
                "About" -> {
                    Text(
                        text = "More Info",
                        color = Color(0xFF64B5F6),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .clickable { onAboutClick() }
                            .padding(8.dp)
                    )
                }
            }


        }
    }
}



@Composable
fun ActionButtons(context: Context, navController: NavController) {
    val activity = context as? Activity
    OutlinedButton(
        onClick = {
            logoutUser(context)
            activity?.recreate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White,
            backgroundColor = Color(0xFF1E3C72)
        ),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Text(text = "Logout", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

data class SettingItem(val title: String, val icon: ImageVector)
