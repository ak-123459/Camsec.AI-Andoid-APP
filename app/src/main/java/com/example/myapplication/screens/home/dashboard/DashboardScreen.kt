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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import com.example.myapplication.local.repository.decodeBase64ToBitmap
import com.example.myapplication.local.repository.isNetworkAvailable
import com.example.myapplication.network.GetStudentByParentCode
import com.example.myapplication.viewModels.DashboardUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




@OptIn(DelicateCoroutinesApi::class)
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

    // 1. Rely ONLY on the uiState from the ViewModel.
    // This is the single source of truth for your UI.
    val uiState by faceViewModel.uiState.collectAsState()

    // 2. Create a coroutine scope for one-time events,
    // like button clicks.
    val scope = rememberCoroutineScope()

    // 3. Fetch data once when the screen is first composed.
    // The if condition handles network availability and token existence.
    LaunchedEffect(Unit) {
        if (isNetworkAvailable(context) && accessToken != null) {
            faceViewModel.fetchFaceData(GetStudentByParentCode(parentCode), accessToken)
        }
    }



    NavHost(
        navController = dashboardNavController,
        startDestination = "dashboard_list",
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        composable("dashboard_list") {
            // Use a single Box with a when statement to handle all UI states.
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is DashboardUiState.Loading -> {
                        // Display a loading indicator.
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                strokeWidth = 5.dp,
                                color = Color(0xFF1E88E5),
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Loading students...")
                        }
                    }
                    is DashboardUiState.Success -> {
                        // 4. Correctly display the LazyRow inside the Success state.
                        val students = state.students
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Animated Header (same as before)
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
                                        text = "",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // LazyRow with student items.
                            LazyRow(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(students, key = { it.id!! }) { face ->
                                    StudentItems(face = face) {
                                        // 5. Use the coroutine scope here to navigate
                                        // or perform other suspend functions.
                                        scope.launch {
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
                        }
                    }
                    is DashboardUiState.Error -> {
                        // Display an error message with a retry button.
                        val errorMessage = state.message
                        ErrorOrNoInternetUI(
                            message = errorMessage,
                            icon = Icons.Default.Error,
                            onRetry = {
                                // Use the correct coroutine scope for the retry button click.
                                scope.launch {
                                    if (accessToken != null) {
                                        faceViewModel.fetchFaceData(GetStudentByParentCode(parentCode), accessToken)
                                    }
                                }
                            }
                        )
                    }
                    is DashboardUiState.NoInternet -> {
                        // Display a no-internet message with a retry button.
                        ErrorOrNoInternetUI(
                            message = "No internet connection",
                            icon = Icons.Default.WifiOff,
                            onRetry = {
                                scope.launch {
                                    if (accessToken != null) {
                                        faceViewModel.fetchFaceData(GetStudentByParentCode(parentCode), accessToken)
                                    }
                                }
                            }
                        )
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

// A reusable composable for displaying errors or no-internet messages.
@Composable
fun ErrorOrNoInternetUI(
    message: String,
    icon: ImageVector,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(message, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text("Retry")
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