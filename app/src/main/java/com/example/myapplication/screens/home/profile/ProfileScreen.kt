package com.example.myapplication.screens.home.profile

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val email = authShardPref.getEmail(context)
    val full_name = authShardPref.getFullName(context)


    Scaffold(

        backgroundColor = Color.White

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ProfileHeaderSection(email,full_name)
            Spacer(modifier = Modifier.height(16.dp))
            ProfileActionsGrid(navController,context)
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryActionButtons(authShardPref,context,navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Composable
fun ProfileHeaderSection(email: String?,full_name:String?) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rose_petals), // Replace with your own image
            contentDescription = "Profile background",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Filled.Person, // Replace with profile image
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = full_name.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = email.toString(),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun ProfileActionsGrid(navController: NavController,context: Context) {
    val actions = listOf(
        ActionItem("Notifications", Icons.Default.Notifications),
        ActionItem("Subscriptions", Icons.Default.Star),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(actions) { action ->
            ActionCard(action,navController, context = context)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionCard(action: ActionItem,navController: NavController,context: Context) {
    var dialogeTitle = remember { mutableStateOf("") }
    var dialogeMessage = remember { mutableStateOf("") }
    val showInfoDialog = remember { mutableStateOf(false) }

    // This if block ensures the dialog is only composed when the state is true
    if (showInfoDialog.value) {
        AlertDialog(
            onDismissRequest = { showInfoDialog.value = false },
            title = { Text(dialogeTitle.value) },
            text = {
                Text(text = dialogeMessage.value)
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    Card(
        onClick = {

            if (action.title == "Notifications") {

                 // 2. Create an Intent for the target activity
                val intent = Intent(context, MainActivity::class.java)
                // 3. Start the activity using the context
                context.startActivity(intent)
                
            }
            else if (action.title == "Subscriptions") {
                    showInfoDialog.value = true
                    dialogeTitle.value = " ⚠\uFE0F Subscriptions"
                    dialogeMessage.value = "There is no Subscriptions available."
                }


        },
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.Black,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun PrimaryActionButtons(authShardPref:SecurePrefsManager,context:Context,navController: NavController) {

    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {


        OutlinedButton(
            onClick = {
                logoutUser(context)
                activity?.recreate()
                navController.navigate("login"){

                    popUpTo(0) { inclusive = true }

                }

                      },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White, backgroundColor = MaterialTheme.colors.primary),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(text = "Logout", fontSize = 18.sp, color = Color.White)
        }
    }
}


data class ActionItem(val title: String, val icon: ImageVector)








@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Show_Preview() {
ProfileScreen(navController = NavController(LocalContext.current))
}
