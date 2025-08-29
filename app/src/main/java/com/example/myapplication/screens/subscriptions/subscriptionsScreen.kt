package com.example.myapplication.screens.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class Plan(
    val title: String,
    val subtitle: String,
    val price: String,
    val features: List<String>,
    val cardColor: Color,
    val buttonColor: Color,
)



data class PricingOption(val duration: String, val price: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricingScreen() {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedPlan by remember { mutableStateOf<Plan?>(null) }

    // Define the data for each plan
    val starterPlan = Plan(
        title = "Free",
        subtitle = "Perfect for beginners",
        price = "₹0/Monthly",
        features = listOf(
            "Today's Attendance",
            "Single device Access",
            "Student And Parent Information's"
        ),
        cardColor = Color(0xFF6200EE),
        buttonColor = Color(0xFF0079C1),
    )

    val proPlan = Plan(
        title = "Pro",
        subtitle = "Access to premium features",
        price = "₹149/Month.₹599/6 Months.₹1199/Year",
        features = listOf(
            "Unlimited devices",
            "Full Attendance History",
            "Basic Notifications",
        ),
        cardColor = Color(0xFFFFCC3543),
        buttonColor = Color(0xFFFFA5454500),
    )

    val premiumPlan = Plan(
        title = "Premium",
        subtitle = "All features unlocked",
        price = "₹199/Month.₹699/6 Months.₹1299/Year",
        features = listOf(
            "All Pro features",
            "Advance Analytics",
            "Real-time Advance alerts",
        ),
        cardColor = Color(0xFF9B59B6),
        buttonColor = Color(0xFFC044B2),
    )

    if (selectedPlan != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                selectedPlan = null
            },
            sheetState = sheetState
        ) {
            selectedSubcription(plan = selectedPlan!!)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF0F0F0) // Set the background color here
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose Your Journey!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            // Plan cards are arranged vertically
            PlanCard(plan = starterPlan, onClick = {
                selectedPlan = starterPlan
                scope.launch { sheetState.show() }
            })
            PlanCard(plan = proPlan, onClick = {
                selectedPlan = proPlan
                scope.launch { sheetState.show() }
            })
            PlanCard(plan = premiumPlan, onClick = {
                selectedPlan = premiumPlan
                scope.launch { sheetState.show() }
            })
        }

    }
}

// PlanCard and FeatureItem composables remain unchanged
@Composable
fun PlanCard(plan: Plan, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(plan.cardColor)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = plan.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = plan.subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
            Text(
                text = plan.price,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                plan.features.forEach { feature ->
                    FeatureItem(featureText = feature)
                }
            }
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = plan.buttonColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Select",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun FeatureItem(featureText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Feature available",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = featureText,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            lineHeight = 16.sp
        )
    }
}



@Composable
fun selectedSubcription(plan: Plan) {
    val primaryColor = MaterialTheme.colors.onSurface
    val backgroundColor = Color(0xFFFFE0B2)
    var selectedPricing by remember { mutableStateOf<PricingOption?>(null) }

    // Parse the price string to get pricing options
    val priceList = remember(plan.price) {
        plan.price.split(".").map {
            val parts = it.split("/")
            if (parts.size == 2) {
                PricingOption(duration = parts[1].trim(), price = parts[0].trim())
            } else {
                null
            }
        }.filterNotNull()
    }

    LaunchedEffect(priceList) {
        if (priceList.isNotEmpty()) {
            selectedPricing = priceList[0]
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = plan.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = primaryColor,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plan.subtitle,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))

            priceList.forEach { option ->
                val isSelected = selectedPricing == option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .selectable(
                            selected = isSelected,
                            onClick = { selectedPricing = option },
                            role = androidx.compose.ui.semantics.Role.RadioButton
                        )
                        .semantics { selected = isSelected },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { selectedPricing = option },
                        colors = RadioButtonDefaults.colors(selectedColor = primaryColor)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = option.price,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = option.duration,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                if (priceList.indexOf(option) < priceList.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = .5.dp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "${plan.title} provides more benefits:",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            plan.features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Benefit",
                        tint = primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feature, fontSize = 14.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Handle purchase */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Subscribe to ${selectedPricing?.duration ?: "Plan"}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun showPreview11(){

PricingScreen()

}