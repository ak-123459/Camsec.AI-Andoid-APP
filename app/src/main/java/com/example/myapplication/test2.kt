package com.example.myapplication






import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.network.AttendanceWeekly


@Composable
fun AttendanceDashboard(
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

//
//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun PreviewDashboard() {
//    AttendanceDashboard()
//}

