package com.example.myapplication.screens.home.dashboard

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.myapplication.viewModels.StudentDetailsViewModel
import com.example.myapplication.utility.SecurePrefsManager
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import com.example.myapplication.R
import com.example.myapplication.local.repository.decodeBase64ToBitmap
import com.example.myapplication.local.repository.isNetworkAvailable
import com.example.myapplication.network.AttendanceWeekly
import com.example.myapplication.network.GetStudentByParentCode
import com.example.myapplication.network.StudentDetails
import com.example.myapplication.viewModels.AttendanceViewModel
import com.example.myapplication.viewModels.DashboardUiState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen() {
//    val dashboardNavController = rememberNavController()
    val secPref = SecurePrefsManager
    val context = LocalContext.current
    val faceViewModel: StudentDetailsViewModel = viewModel()
    val parentCode = secPref.getParentCode(context)
    val accessToken = secPref.getToken(context)
    val attendanceViewModel: AttendanceViewModel = viewModel()



    // 1. Rely ONLY on the uiState from the ViewModel.
    // This is the single source of truth for your UI.
    val uiState by faceViewModel.uiState.collectAsState()




    // State to hold the selected student
    var selectedStudent by remember { mutableStateOf<StudentDetails?>(null) }


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



//    NavHost(
//        navController = dashboardNavController,
//        startDestination = "dashboard_list",
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF7F9FC))
//            .padding(WindowInsets.systemBars.asPaddingValues())
//    ) {
//        composable("dashboard_list") {
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
                        val students = state.students

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Student tabs inside a rounded Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF4D6296) // semi-transparent background
                                )
                            ) {


                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(students, key = { it.id!! }) { face ->
                                        StudentTab(
                                            face = face,
                                            isSelected = face.id == selectedStudent?.id
                                        ) {
                                            selectedStudent = face

                                        }
                                    }
                                }
                            }

                            // Conditionally render content based on selected student
                            if (selectedStudent != null) {

                                if (accessToken != null) {

                                    StudentDashboardContent(student = selectedStudent!!, viewModel = attendanceViewModel,accessToken)


                                } else{

                                    Text("No Details Available...")
                                }

                            } else {

                                DefaultDashboardContent()
                            }
                        }
                    }

                    is DashboardUiState.Error -> {
                        // Display an error message with a retry button.
                        val errorMessage = state.message
                        ErrorOrNoInternetUI(
                            message = "\uD83C\uDF10 Please try after sometimes...",
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
//        composable(
//            route = "attendance_screen/{studentName}StudentDashboardContent/{stdID}",
//            arguments = listOf(
//                navArgument("studentName") { type = NavType.StringType },
//                navArgument("stdID") { type = NavType.IntType }
//            )
//        ) { backStackEntry ->
//            val userName = backStackEntry.arguments?.getString("studentName")
//            val stdID = backStackEntry.arguments?.getInt("stdID")
//
//            if (userName != null && stdID != null) {
//                AttendanceScreen(faceViewModel, userName, stdID)
//            }
//        }
//    }



// Create a new composable for the content shown when a student is selected
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentDashboardContent(student: StudentDetails,viewModel: AttendanceViewModel,accessToken: String) {

    val weeklyStats by viewModel.weeklyStats.observeAsState()
    val todayPresent by viewModel.queryDayStats.observeAsState()
    val monthlySummary by viewModel.monthlySummary.observeAsState()
    val isLoading by viewModel.isLoading
    val today = java.time.LocalDate.now().toString()


    // Trigger fetch when screen is shown
    LaunchedEffect(Unit) {
        student.id?.let { viewModel.fetchAttendancesStats(it, today, accessToken) }
    }


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Dashboard for ${student.full_name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Here you would add the specific UI for the selected student
        // For example:
        // AttendaAttendanceDashboardnceHistory(studentId = student.id)
        // StudentEvents(studentId = student.id)
    }


    StudentAttendanceDashboard(
        weeklyStats = weeklyStats,
        todayPresent = todayPresent?.attendance?.present,
        monthlyPresent = monthlySummary?.present_count ?: 0,
        monthlyAbsent = monthlySummary?.absent_count ?: 0,
        isLoading = isLoading
    )

}


// Create a new composable for the default dashboard
@Composable
fun DefaultDashboardContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Welcome to the Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
        // Here you would add the default widgets from your original UI
        // like "Attendance Summary", "Upcoming Events", etc.
    }





}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceScreen(
    stdId: Int,
    accessToken: String,
    viewModel: AttendanceViewModel = viewModel()
) {


}



@Composable
fun StudentTab(face: StudentDetails, isSelected: Boolean, onClick: () -> Unit) {
    // Animate the size and border for a smooth visual effect.
    val animatedImageSize by animateDpAsState(targetValue = if (isSelected) 130.dp else 90.dp, label = "image_size")
    val animatedBorderWidth by animateDpAsState(targetValue = if (isSelected) 6.dp else 3.dp, label = "border_width")

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the student's image from the Base64 string
        face.image?.let { base64Image ->
            val bitmap = remember(base64Image) { decodeBase64ToBitmap(base64Image) }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Profile picture of ${face.full_name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(animatedImageSize)
                        .clip(CircleShape)
                        .border(
                            width = animatedBorderWidth,
                            color = if (isSelected) Color.Cyan else Color.White,
                            shape = CircleShape
                        )
                        .background(Color.White)
                )
            }
        }

        // Add a space between the image and the text.
        Spacer(modifier = Modifier.height(8.dp))

        // Display the student's name
        Text(
            text = face.full_name ?: "Student",
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color.Yellow else MaterialTheme.colorScheme.onPrimary
        )


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
fun StudentAttendanceDashboard(
    weeklyStats: List<AttendanceWeekly?>?,
    todayPresent: Int?,
    monthlyPresent: Int,
    monthlyAbsent: Int,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TodayAttendanceCard(isLoading = isLoading, isPresent = todayPresent)
        WeeklyAttendanceCard(isLoading = isLoading, weeklyStats = weeklyStats)
        MonthlySummaryCard(isLoading = isLoading, present = monthlyPresent, absent = monthlyAbsent)
    }
}



@Composable
fun TodayAttendanceCard(isLoading: Boolean, isPresent: Int?) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isLoading -> MaterialTheme.colorScheme.surfaceVariant
                isPresent == 1 -> Color(0xFFE8FCEB)
                else -> Color(0xFFFDEAEA)
            }
        ),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Attendance info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (isPresent == 1) Color(0xFF16A34A) else Color(0xFFDC2626),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPresent == 1) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Today's Attendance",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (isPresent == 1) "Present" else "Absent",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isPresent == 1) Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        )
                    }
                }

                HistoryActionCard(onClick = { /* navigate */ })
            }
        }
    }
}



@Composable
fun HistoryActionCard(
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(top = 8.dp)
            .width(100.dp) // keeps it compact
            .clickable { onClick() }
    ) {
        // Circular history icon
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(56.dp),
            shadowElevation = 4.dp
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.historyicons), // your history icon
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize().padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "History",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "See records",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
@Composable
fun MonthlySummaryCard(isLoading: Boolean, present: Int, absent: Int) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Monthly Summary",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            Spacer(Modifier.height(20.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val percentage = if ((present + absent) > 0) {
                    (present * 100) / (present + absent)
                } else 0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SummaryStat("$present", "Present", Color(0xFF22C55E))
                    SummaryStat("$absent", "Absent", Color(0xFFEF4444))
                    SummaryStat("$percentage%", "Attendance", Color(0xFF2563EB))
                }
            }
        }
    }
}



@Composable
fun SummaryStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
            )
        )
    }
}




@Composable
fun WeeklyAttendanceCard(isLoading: Boolean, weeklyStats: List<AttendanceWeekly?>?) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Weekly Attendance",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            Spacer(Modifier.height(20.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                weeklyStats.isNullOrEmpty() -> {
                    Text(
                        text = "No weekly stats available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklyStats.forEach { stat ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Canvas(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .width(20.dp)
                                ) {
                                    if (stat != null) {
                                        drawRoundRect(
                                            color = if (stat.status) Color(0xFF22C55E) else Color(0xFFEF4444),
                                            topLeft = Offset(0f, size.height / 3),
                                            size = Size(size.width, size.height * 0.7f),
                                            cornerRadius = CornerRadius(12f, 12f)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                if (stat != null) {
                                    Text(
                                        text = stat.day_name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}










@Composable
fun Hello(dashboardNavController: NavHostController) {

    Text("Hello World")

}