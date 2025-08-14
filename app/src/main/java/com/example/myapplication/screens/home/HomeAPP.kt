package com.example.myapplication.screens.home

import Navigation
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp() {

    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val corutine  = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                onMenuClick = {
                   corutine.launch {
                    scaffoldState.drawerState.open() }
                }
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
