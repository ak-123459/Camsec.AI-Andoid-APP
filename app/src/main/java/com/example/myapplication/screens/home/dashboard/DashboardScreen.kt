package com.example.myapplication.screens.home.dashboard

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.R
import com.example.myapplication.screens.components.DemoFaceItem
import com.example.myapplication.screens.components.StudentItems
import com.example.myapplication.screens.home.dashboard.attandance_summary_screen.AttendanceScreen
import com.example.myapplication.viewModels.StudentDetailsViewModel
import com.example.myapplication.utility.SecurePrefsManager
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import com.example.myapplication.local.repository.decodeBase64ToBitmap
import com.example.myapplication.network.GetStudentByParentCode
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen() {
    val dashboardNavController = rememberNavController()
    val secPref = SecurePrefsManager
    val context = LocalContext.current
    val faceViewModel: StudentDetailsViewModel = viewModel()
    val parentCode = secPref.getParentCode(context)
    val accessToken = secPref.getToken(context)

    var seconds by remember { mutableStateOf(0) }
    var showProgress by remember { mutableStateOf(true) }

    val faces by faceViewModel.faces.observeAsState(emptyList())
    val error by faceViewModel.error.observeAsState()

    // Fetch data once
    LaunchedEffect(Unit) {
        if (faces.isEmpty() && accessToken != null) {
            faceViewModel.fetchFaceData(GetStudentByParentCode(parentCode), accessToken)
        }
    }

    // Timer → show loader for 5 sec
    LaunchedEffect(Unit) {
        delay(3000L)
        seconds = 5
        showProgress = false
    }

    NavHost(navController = dashboardNavController, startDestination = "dashboard_list") {
        composable("dashboard_list") {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F9FC)) // Softer bg
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                Scaffold(
                    modifier = Modifier.align(Alignment.TopCenter),
                    containerColor = Color.Transparent
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when {
                            showProgress -> {
                                // 🔹 Animated Loading
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        strokeWidth = 5.dp,
                                        color = Color(0xFF1E88E5),
                                        modifier = Modifier
                                            .size(60.dp)
                                            .animateContentSize()
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Loading students...",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            error != null || (faces.isEmpty() && seconds >= 5) -> {
                                // 🔹 Fade-in Empty State
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn(animationSpec = tween(600)),
                                    exit = fadeOut()
                                ) {
                                    EmptyChildStateScreen()
                                }
                            }

                            else -> {
                                // 🔹 Animated Success Layout
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    // 🔹 Animated Header
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
                                        exit = fadeOut()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
                                                    )
                                                )
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Students",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            TextButton(onClick = { /* your see all action */ }) {
                                                Text(
                                                    text = "See All",
                                                    fontSize = 14.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // 🔹 Animated Student List
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(
                                            items = faces,
                                            key = { it.id!! }
                                        ) { face ->
                                            AnimatedVisibility(
                                                visible = true,
                                                enter = fadeIn() + scaleIn(),
                                                exit = fadeOut()
                                            ) {
                                                StudentItems(face = face) {
                                                    face.image?.let { img ->
                                                        decodeBase64ToBitmap(img)
                                                    }?.let { bmp ->
                                                        faceViewModel.setStudentImage(bmp)
                                                    }
                                                    dashboardNavController.navigate(
                                                        "attendance_screen/${face.full_name}/${face.id}"
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                }
                            }
                        }
                    }
                }
            }
        }

        composable(
            route = "attendance_screen/{studentName}/{stdID}",
            arguments = listOf(
                navArgument("studentName") { type = NavType.StringType },
                navArgument("stdID") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("studentName")
            val stdID = backStackEntry.arguments?.getInt("stdID")

            if (userName != null && stdID != null) {
                AttendanceScreen(faceViewModel, userName, stdID)
            }
        }
    }
}




@Composable
fun EmptyChildStateScreen(
    title: String = "No child profiles linked yet",
    message: String = "Please Contact to your institutes.",
    icon: ImageVector = Icons.Filled.SentimentVeryDissatisfied,
    onPrimaryCtaClick: () -> Unit = {},
    onSecondaryCtaClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // A small playful scale animation for the illustration
    var played by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (played) 1f else 0.85f)
    LaunchedEffect(Unit) { // trigger after first composition
        played = true
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular icon container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Empty state illustration",
                    modifier = Modifier
                        .size(64.dp)
                        .scale(scale),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = title }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .semantics { contentDescription = message }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary CTA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
            }

            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.height(28.dp))

            // Helpful micro-copy
            Text(
                text = "Tip: To add childrens please contact to your child institute.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        }
    }
}






@Composable
fun Hello(dashboardNavController: NavHostController) {

    Text("Hello World")

}