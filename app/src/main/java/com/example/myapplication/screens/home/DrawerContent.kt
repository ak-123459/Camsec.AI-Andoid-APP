package com.example.myapplication.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(navController: NavController, scaffoldState: ScaffoldState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("MENU", modifier = Modifier.padding(16.dp), fontSize = 25.sp,style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

        Divider(modifier = Modifier.padding(2.dp), color = Color.Gray, thickness = 1.dp)

        DrawerItem("Dashboard", "dashboard", navController, scaffoldState)
        DrawerItem("Profile", "profile", navController, scaffoldState)
        DrawerItem("Settings", "settings", navController, scaffoldState)
    }
}


@Composable
fun DrawerItem(
    label: String,
    route: String,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Text(fontSize = 20.sp,
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (currentRoute != route) {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            }
            .padding(16.dp)
    )
}
