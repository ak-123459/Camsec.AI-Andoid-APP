package com.example.myapplication

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.home.notifications.NotificationListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModels.NotificationViewModel
import com.example.myapplication.viewModels.NotificationViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext as Application

    val viewModel: NotificationViewModel = viewModel(
        factory = NotificationViewModelFactory(context)
    )


    NavHost(
        navController = navController,
        startDestination = "notifications"
    ) {

        composable("notifications") {
            NotificationListScreen(viewModel)
        }

    }
}
