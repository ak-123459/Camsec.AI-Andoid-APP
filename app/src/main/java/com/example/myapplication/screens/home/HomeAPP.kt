package com.example.myapplication.screens.home

import Navigation
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DemoFaceDetailsViewModel
import com.example.myapplication.FaceDetailsViewModel
import kotlinx.coroutines.launch

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
