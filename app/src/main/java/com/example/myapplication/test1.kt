////package com.example.myapplication
////
////import android.annotation.SuppressLint
////import android.os.Bundle
////import androidx.activity.ComponentActivity
////import androidx.activity.compose.setContent
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.rememberScrollState
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.foundation.verticalScroll
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.clip
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.text.style.TextAlign
////import androidx.compose.ui.tooling.preview.Preview
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.Done
////
////// Data class to represent a pricing plan
////data class Plan(
////    val title: String,
////    val subtitle: String,
////    val price: String,
////    val features: List<String>,
////    val cardColor: Color,
////    val buttonColor: Color,
////)
////
////@SuppressLint("InvalidColorHexValue")
////@Composable
////fun PricingScreen() {
////    // Define the data for each plan with colors matching the image
////    val starterPlan = Plan(
////        title = "Free",
////        subtitle = "Perfect for beginners",
////        price = "₹0/Monthly",
////        features = listOf(
////            "Today's Attendance",
////            "Single device Access",
////            "Student And Parent Information's"
////        ),
////        cardColor = Color(0xFF5AC8E8), // Light blue/cyan
////        buttonColor = Color(0xFF0079C1), // Darker blue
////    )
////
////    val proPlan = Plan(
////        title = "Pro",
////        subtitle = "Access to premium features",
////        price = "₹149/Month.₹599/6 Months.₹1199/Year",
////        features = listOf(
////            "Unlimited devices",
////            "Full Attendance History",
////            "Basic Notifications",
////        ),
////        cardColor = Color(0xFFFFCC3543), // Yellow
////        buttonColor = Color(0xFFFFA5454500), // Orange
////    )
////
////    val premiumPlan = Plan(
////        title = "Premium",
////        subtitle = "All features unlocked",
////        price = "₹199/Month.₹699/6 Months.₹1299/Year",
////        features = listOf(
////            "All Pro features",
////            "Advance Analytics",
////            "Real-time Advance alerts",
////        ),
////        cardColor = Color(0xFF9B59B6), // Purple
////        buttonColor = Color(0xFFC044B2), // Pink
////    )
////
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .background(Color(0xFFF0F0F0)) // Light gray background
////            .padding(16.dp)
////            .verticalScroll(rememberScrollState()),
////        horizontalAlignment = Alignment.CenterHorizontally,
////        verticalArrangement = Arrangement.spacedBy(16.dp)
////    ) {
////        Text(
////            text = "Choose Your Journey!",
////            fontSize = 24.sp,
////            fontWeight = FontWeight.Bold,
////            modifier = Modifier.padding(bottom = 8.dp),
////            textAlign = TextAlign.Center
////        )
////        // Plan cards are arranged vertically
////        PlanCard(plan = starterPlan)
////        PlanCard(plan = proPlan)
////        PlanCard(plan = premiumPlan)
////    }
////}
////
////
////@Composable
////fun PlanCard(plan: Plan, modifier: Modifier = Modifier) {
////
////    Box(
////        modifier = modifier
////            .fillMaxWidth()
////            .clip(RoundedCornerShape(20.dp))
////            .background(plan.cardColor)
////            .padding(24.dp)
////    ) {
////        Column(
////            horizontalAlignment = Alignment.CenterHorizontally,
////            modifier = Modifier.fillMaxWidth()
////        ) {
////
////            Text(
////                text = plan.title,
////                fontSize = 20.sp,
////                fontWeight = FontWeight.Bold,
////                color = Color.White,
////                textAlign = TextAlign.Center
////            )
////            Text(
////                text = plan.subtitle,
////                fontSize = 12.sp,
////                color = Color.White.copy(alpha = 0.8f),
////                textAlign = TextAlign.Center,
////                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
////            )
////            Text(
////                text = plan.price,
////                fontSize = 15.sp,
////                fontWeight = FontWeight.Bold,
////                color = Color.White,
////                textAlign = TextAlign.Center,
////                modifier = Modifier.padding(bottom = 20.dp)
////            )
////            Column(
////                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
////            ) {
////                plan.features.forEach { feature ->
////                    FeatureItem(featureText = feature)
////                }
////            }
////            Button(
////                onClick = { /* Handle button click */ },
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .height(50.dp),
////                shape = RoundedCornerShape(10.dp),
////                colors = ButtonDefaults.buttonColors(
////                    containerColor = plan.buttonColor,
////                    contentColor = Color.White
////                )
////            ) {
////                Text(
////                    text = "Select",
////                    fontWeight = FontWeight.SemiBold,
////                    fontSize = 16.sp
////                )
////            }
////        }
////    }
////}
////
////@Composable
////fun FeatureItem(featureText: String) {
////    Row(
////        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
////        verticalAlignment = Alignment.CenterVertically
////    ) {
////        Icon(
////            imageVector = Icons.Default.Done,
////            contentDescription = "Feature available",
////            tint = Color.White,
////            modifier = Modifier.size(16.dp)
////        )
////        Spacer(modifier = Modifier.width(8.dp))
////        Text(
////            text = featureText,
////            color = Color.White.copy(alpha = 0.9f),
////            fontSize = 14.sp,
////            lineHeight = 16.sp
////        )
////    }
////}
////
////@Preview(showBackground = true)
////@Composable
////fun DefaultPreview() {
////    MaterialTheme {
////        PricingScreen()
////    }
////}
//
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.drawText
//import androidx.compose.ui.text.rememberTextMeasurer
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import com.example.myapplication.network.AttendanceMonthly
//import com.example.myapplication.network.AttendanceWeekly
//import com.example.myapplication.viewModels.AttendanceViewModel
//
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun AttendanceDashboard(viewModel: AttendanceViewModel, studentId: Int, accessToken: String) {
//    val isWeeklyView = remember { mutableStateOf(true) }
//
//    // Observe LiveData properly
//    val attendanceWeekly by viewModel.attendanceWeekly.observeAsState()
//    val attendanceMonthly by viewModel.attendanceMonthly.observeAsState()
//    val isLoading = viewModel.isLoading.value
//    val error by viewModel.errorMessage.observeAsState()
//
//    // Determine which data to show
//    val attendanceData = if (isWeeklyView.value) {
//        attendanceWeekly?.let { listOf(it) } ?: emptyList() // wrap single object in a list
//    } else {
//        attendanceMonthly?.let { listOf(it) } ?: emptyList()
//    }
//
//    Surface(
//        color = Color(0xFFF3F4F6),
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Student Attendance",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black,
//                modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
//            )
//
//            // Buttons to switch view
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Button(
//                    onClick = {
//                        isWeeklyView.value = true
//                        viewModel.loadWeekly(studentId, java.time.LocalDate.now().toString(), accessToken)
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isWeeklyView.value) Color(0xFF6366F1) else Color(0xFFE5E7EB),
//                        contentColor = if (isWeeklyView.value) Color.White else Color.Black
//                    )
//                ) { Text("Weekly") }
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Button(
//                    onClick = {
//                        isWeeklyView.value = false
//                        viewModel.loadMonthly(studentId, java.time.LocalDate.now().toString(), accessToken)
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (!isWeeklyView.value) Color(0xFF6366F1) else Color(0xFFE5E7EB),
//                        contentColor = if (!isWeeklyView.value) Color.White else Color.Black
//                    )
//                ) { Text("Monthly") }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            } else if (!error.isNullOrEmpty()) {
//                Text(
//                    text = error ?: "Unknown error",
//                    color = Color.Red,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            } else {
//                // Show chart only if no error
//                BarChart(attendanceData, isWeeklyView.value)
//            }
//
//        }
//    }
//}
//
//
//// BarChart updated to handle weekly/monthly data
//@Composable
//fun BarChart(data: List<Any>, isWeekly: Boolean) {
//    val scrollState = rememberScrollState()
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .horizontalScroll(scrollState)
//            .height(250.dp),
//        verticalAlignment = Alignment.Bottom
//    ) {
//        data.forEach { item ->
//            val label: String
//            val isPresent: Boolean
//
//            if (isWeekly && item is AttendanceWeekly) {
//                label = item.day_name
//                isPresent = item.present
//            } else if (!isWeekly && item is AttendanceMonthly) {
//                label = item.month_name ?: "Unknown"
//                // Use total_days_present as "present" for bar height logic
//                isPresent = item.total_days_present > 0
//            } else {
//                return@forEach
//            }
//
//            val barHeight = if (isPresent) 150.dp else 50.dp
//            val barColor = if (isPresent) Color(0xFF22C55E) else Color(0xFFEF4444)
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.padding(horizontal = 4.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .width(20.dp)
//                        .height(barHeight)
//                        .background(barColor, shape = RoundedCornerShape(4.dp))
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = label,
//                    fontSize = 10.sp,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}
//
//
//
