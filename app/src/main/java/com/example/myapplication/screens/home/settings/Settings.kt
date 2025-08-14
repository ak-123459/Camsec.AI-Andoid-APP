package com.example.myapplication.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.screens.components.AppDetailsBottomSheet
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.utility.logoutUser

@Composable
fun SettingsScreen(navController: NavController) {


    val authShardPref = SecurePrefsManager
    val context = LocalContext.current
    // State to manage the visibility of the bottom sheet
    var bottomSheetState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                backgroundColor = Color.Black,
                elevation = 0.dp
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

            // Settings List
            SettingsList(
                onAboutClick = { bottomSheetState = true } // Open bottom sheet on About click
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            ActionButtons(context,navController)
        }
    }

    // Bottom Sheet to show app details
    if (bottomSheetState) {
        AppDetailsBottomSheet(
            onDismiss = { bottomSheetState = false }
        )
    }
}


@Composable
fun SettingsList(onAboutClick: () -> Unit) {
    val settings = listOf(
        SettingItem("Notifications", Icons.Default.Notifications),
        SettingItem("Privacy", Icons.Default.Lock),
        SettingItem("Account", Icons.Default.AccountCircle),
        SettingItem("About", ImageVector.vectorResource(R.drawable.about)),

        )

    Column(modifier = Modifier.fillMaxWidth()) {
        settings.forEach { setting ->
            SettingItemCard(setting,onAboutClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SettingItemCard(setting: SettingItem,onAboutClick: () -> Unit) {
    var isToggled by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
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
                    color = Color.White
                )
            }

            // Toggle for settings like Notifications
            if (setting.title == "Notifications") {
                Switch(
                    checked = isToggled,
                    onCheckedChange = { isToggled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, disabledCheckedTrackColor = Color.Gray, uncheckedThumbColor = Color.Gray, uncheckedTrackColor = Color.Cyan)
                )
            }

            if (setting.title == "About") {
                Box(
                    modifier = Modifier
                        .clickable(onClick = { onAboutClick() })
                        .padding(16.dp)
                ) {
                    Text(text = "More Info", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}



@Composable
fun ActionButtons( context: Context, navController: NavController) {

    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { /* Handle Change Password */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Change Password", color = Color.White, fontSize = 18.sp)
        }

        OutlinedButton(
            onClick = {
                logoutUser(context)
                activity?.recreate()
                      },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White, backgroundColor = MaterialTheme.colors.primary),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Text(text = "Logout", fontSize = 18.sp, color = Color.White)
        }
    }
}

data class SettingItem(val title: String, val icon: ImageVector)
//
//@Preview(showBackground = true)
//@Composable
//fun SettingsPreview() {
//    SettingsScreen()
//}
