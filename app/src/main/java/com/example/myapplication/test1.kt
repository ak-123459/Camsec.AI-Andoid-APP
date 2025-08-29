//package com.example.myapplication
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Done
//
//// Data class to represent a pricing plan
//data class Plan(
//    val title: String,
//    val subtitle: String,
//    val price: String,
//    val features: List<String>,
//    val cardColor: Color,
//    val buttonColor: Color,
//)
//
//@SuppressLint("InvalidColorHexValue")
//@Composable
//fun PricingScreen() {
//    // Define the data for each plan with colors matching the image
//    val starterPlan = Plan(
//        title = "Free",
//        subtitle = "Perfect for beginners",
//        price = "₹0/Monthly",
//        features = listOf(
//            "Today's Attendance",
//            "Single device Access",
//            "Student And Parent Information's"
//        ),
//        cardColor = Color(0xFF5AC8E8), // Light blue/cyan
//        buttonColor = Color(0xFF0079C1), // Darker blue
//    )
//
//    val proPlan = Plan(
//        title = "Pro",
//        subtitle = "Access to premium features",
//        price = "₹149/Month.₹599/6 Months.₹1199/Year",
//        features = listOf(
//            "Unlimited devices",
//            "Full Attendance History",
//            "Basic Notifications",
//        ),
//        cardColor = Color(0xFFFFCC3543), // Yellow
//        buttonColor = Color(0xFFFFA5454500), // Orange
//    )
//
//    val premiumPlan = Plan(
//        title = "Premium",
//        subtitle = "All features unlocked",
//        price = "₹199/Month.₹699/6 Months.₹1299/Year",
//        features = listOf(
//            "All Pro features",
//            "Advance Analytics",
//            "Real-time Advance alerts",
//        ),
//        cardColor = Color(0xFF9B59B6), // Purple
//        buttonColor = Color(0xFFC044B2), // Pink
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF0F0F0)) // Light gray background
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Text(
//            text = "Choose Your Journey!",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 8.dp),
//            textAlign = TextAlign.Center
//        )
//        // Plan cards are arranged vertically
//        PlanCard(plan = starterPlan)
//        PlanCard(plan = proPlan)
//        PlanCard(plan = premiumPlan)
//    }
//}
//
//
//@Composable
//fun PlanCard(plan: Plan, modifier: Modifier = Modifier) {
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(20.dp))
//            .background(plan.cardColor)
//            .padding(24.dp)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//
//            Text(
//                text = plan.title,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = plan.subtitle,
//                fontSize = 12.sp,
//                color = Color.White.copy(alpha = 0.8f),
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
//            )
//            Text(
//                text = plan.price,
//                fontSize = 15.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(bottom = 20.dp)
//            )
//            Column(
//                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
//            ) {
//                plan.features.forEach { feature ->
//                    FeatureItem(featureText = feature)
//                }
//            }
//            Button(
//                onClick = { /* Handle button click */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                shape = RoundedCornerShape(10.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = plan.buttonColor,
//                    contentColor = Color.White
//                )
//            ) {
//                Text(
//                    text = "Select",
//                    fontWeight = FontWeight.SemiBold,
//                    fontSize = 16.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FeatureItem(featureText: String) {
//    Row(
//        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = Icons.Default.Done,
//            contentDescription = "Feature available",
//            tint = Color.White,
//            modifier = Modifier.size(16.dp)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = featureText,
//            color = Color.White.copy(alpha = 0.9f),
//            fontSize = 14.sp,
//            lineHeight = 16.sp
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MaterialTheme {
//        PricingScreen()
//    }
//}