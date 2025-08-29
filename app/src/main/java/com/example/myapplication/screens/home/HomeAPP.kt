package com.example.myapplication.screens.home

import Navigation
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.viewModels.NotificationViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp( notificationViewModel: NotificationViewModel= viewModel()) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val corutine  = rememberCoroutineScope()

    // Collect the state from the ViewModel's StateFlow
    val hasNewNotifications by notificationViewModel.hasNewNotifications.collectAsState()


    Scaffold(
        scaffoldState = scaffoldState, modifier = Modifier.navigationBarsPadding(),
        topBar = {
            TopBar(
                onMenuClick = {
                   corutine.launch {
                    scaffoldState.drawerState.open() }
                },hasNewNotifications,context
            )
        },
        drawerContent = {
            DrawerContent(navController, scaffoldState)
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) {
        Navigation(navController)
    }
}
