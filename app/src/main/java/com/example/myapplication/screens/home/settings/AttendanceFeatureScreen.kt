package com.example.myapplication.screens.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Defines the main screen composable for the attendance features UI.
@Composable
fun AttendanceFeaturesScreen() {
    // State variables to manage the on/off status of each feature toggle.
    // `remember` is used to retain the state across recompositions.
    var facialRecognitionEnabled by remember { mutableStateOf(false) }
    var livenessCheckEnabled by remember { mutableStateOf(false) }
    var locationCheckEnabled by remember { mutableStateOf(false) }
    var dailyReportsEnabled by remember { mutableStateOf(false) }

    // Main layout for the screen.
    // Centers the content both horizontally and vertically.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        contentAlignment = Alignment.Center
    ) {
        // The main card container for the UI, with rounded corners and a shadow.
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header section with app title and a user icon.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AttendancePro",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    // User icon placeholder.
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFDBEAFE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Icon",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Section title for the features list.
                Text(
                    text = "Features",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4B5563)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List of feature cards.
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Feature Card for Facial Recognition.
                    FeatureCard(
                        icon = Icons.Default.Face,
                        iconTint = Color(0xFF9333EA), // Purple
                        iconBackground = Color(0xFFF3E8FF), // Purple-100
                        title = "Facial Recognition",
                        subtitle = "Enable face-based check-in and check-out.",
                        isEnabled = facialRecognitionEnabled,
                        onToggle = { facialRecognitionEnabled = it }
                    )

                    // Feature Card for Live Liveness Check.
                    FeatureCard(
                        icon = Icons.Default.Security,
                        iconTint = Color(0xFF16A34A), // Green
                        iconBackground = Color(0xFFD1FAE5), // Green-100
                        title = "Live Liveness Check",
                        subtitle = "Prevent spoofing with live face detection.",
                        isEnabled = livenessCheckEnabled,
                        onToggle = { livenessCheckEnabled = it }
                    )

                    // Feature Card for Geofencing/Location Check.
                    FeatureCard(
                        icon = Icons.Default.LocationOn,
                        iconTint = Color(0xFFDC2626), // Red
                        iconBackground = Color(0xFFFEE2E2), // Red-100
                        title = "Location Check",
                        subtitle = "Ensure attendance is marked from a specific location.",
                        isEnabled = locationCheckEnabled,
                        onToggle = { locationCheckEnabled = it }
                    )

                    // Feature Card for Daily Reports.
                    FeatureCard(
                        icon = Icons.Default.BarChart,
                        iconTint = Color(0xFFF59E0B), // Yellow
                        iconBackground = Color(0xFFFEF9C3), // Yellow-100
                        title = "Daily Reports",
                        subtitle = "Receive daily attendance summaries.",
                        isEnabled = dailyReportsEnabled,
                        onToggle = { dailyReportsEnabled = it }
                    )
                }
            }
        }
    }
}

// A reusable Composable for each feature card.
@Composable
fun FeatureCard(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    title: String,
    subtitle: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB), RoundedCornerShape(16.dp)),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon and text content.
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon background circle.
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Text content (title and subtitle).
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937),
                        fontSize = 16.sp
                    )
                    Text(
                        text = subtitle,
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp
                    )
                }
            }

            // The toggle switch.
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2563EB),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFD1D5DB)
                )
            )
        }
    }
}

// Preview Composable to see the UI in Android Studio's preview pane.
@Preview(showBackground = true)
@Composable
fun PreviewAttendanceScreen() {
    AttendanceFeaturesScreen()
}
