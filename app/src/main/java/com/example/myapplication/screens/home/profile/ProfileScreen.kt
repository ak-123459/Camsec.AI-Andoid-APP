package com.example.myapplication.screens.home.profile

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.screens.auth.AuthAppNavigator
import com.example.myapplication.screens.auth.LoginScreen
import com.example.myapplication.screens.home.notifications.NotificationListScreen
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.utility.logoutUser
import com.example.myapplication.viewModels.NotificationViewModel
import com.example.myapplication.viewModels.NotificationViewModelFactory


@Composable
fun ProfileAppNavHost(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current.applicationContext as Application

    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(context)
    )

    viewModel.logAllNotifications()

    NavHost(navController = navController, startDestination = "profile_screen") {

        // Profile screen
        composable("profile_screen") {
            ProfileScreen(navController)
        }

//        // Nested screens from profile
//        composable("notification_screen") {
//
//            NotificationListScreen(viewModel)
//        }

        composable("login") {

            AuthAppNavigator()
        }


    }
}



@Composable
fun ProfileScreen(navController: NavController) {
    val authShardPref = SecurePrefsManager
    val context = LocalContext.current
    val parentCode = authShardPref.getParentCode(context)
    val fullName = authShardPref.getFullName(context)

    Scaffold(
        backgroundColor = Color(0xFFF8F9FB) // softer bg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ProfileHeaderSection(parentCode, fullName)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
                ProfileActionsGrid(navController, context)
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                PrimaryActionButtons(authShardPref, context, navController)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
@Composable
fun ProfileHeaderSection(email: String?, fullName: String?) {
    // 🔹 Infinite animation controller
    val infiniteTransition = rememberInfiniteTransition(label = "profile_anim")

    // Pulsing scale for profile image
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_anim"
    )

    // Shifting gradient animation
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_shift"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        // 🔹 Animated gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298), Color(0xFF1E3C72)),
                        start = Offset(0f, gradientShift),
                        end = Offset(gradientShift, 0f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Animated profile card
            Card(
                shape = CircleShape,
                elevation = 10.dp,
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🔹 Fade-in animation for texts
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }

            AnimatedVisibility(visible = visible) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = fullName ?: "Guest User",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = email ?: "Unknown Email",
                        fontSize = 14.sp,
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileActionsGrid(navController: NavController, context: Context) {
    val actions = listOf(
        ActionItem("Notifications", Icons.Default.Notifications),
        ActionItem("Subscriptions", Icons.Default.Star),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(actions) { action ->
            ActionCard(action, navController, context)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionCard(action: ActionItem, navController: NavController, context: Context) {
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(dialogTitle) },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Card(
        onClick = {
            when (action.title) {
                "Notifications" -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                "Subscriptions" -> {
                    showInfoDialog = true
                    dialogTitle = "⚠ Subscriptions"
                    dialogMessage = "There is no subscription available."
                }
            }
        },
        shape = RoundedCornerShape(18.dp),
        elevation = 8.dp,
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = Color(0xFF2A5298),
                modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2A2A2A)
            )
        }
    }
}

@Composable
fun PrimaryActionButtons(authShardPref: SecurePrefsManager, context: Context, navController: NavController) {
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = {
                logoutUser(context)
                activity?.recreate()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors( backgroundColor = Color(0xFF2A5298),
                contentColor = Color.White)
        ) {
            Text(text = "Logout", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

data class ActionItem(val title: String, val icon: ImageVector)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Show_Preview() {
    // Dummy preview
    ProfileScreen(navController = NavController(LocalContext.current))
}