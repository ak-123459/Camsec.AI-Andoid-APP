package com.example.myapplication.screens.home.dashboard.attandance_summary_screen

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.viewModels.AttendanceViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.local.repository.decodeBase64ToBitmap
import com.example.myapplication.utility.SecurePrefsManager
import kotlinx.coroutines.CoroutineStart
import kotlin.io.encoding.ExperimentalEncodingApi
import android.util.Base64
import android.util.Log
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.screens.components.AutoDismissAttendanceErrorDialog
import com.example.myapplication.viewModels.StudentDetailsViewModel




@OptIn(ExperimentalEncodingApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceScreen(
    studentDetailsViewModel: StudentDetailsViewModel,
    studentName: String,
    stdID: Int,
    viewModel: AttendanceViewModel = viewModel()
) {
    val attendanceList by viewModel.attendance.observeAsState()
    val context  = LocalContext.current
    val accessToken = SecurePrefsManager.getToken(context)
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage.observeAsState()
    val image = studentDetailsViewModel.studentImage.observeAsState()

    var showError by remember { mutableStateOf(false) }

//    var attendanceStatus by remember { mutableStateOf("Not Marked") }
//    var attendanceTime by remember { mutableStateOf("") }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedDay = remember { mutableStateOf(LocalDate.now()) }

    val currentDate = selectedDate.value
    val year = currentDate.year
    val month = currentDate.month
    val monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.uppercase() }

    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val daysInMonth = month.length(currentDate.isLeapYear)
    val emptyCells = firstDayOfMonth.dayOfWeek.ordinal
    val days = buildList {
        addAll(List(emptyCells) { "" })
        addAll(List(daysInMonth) { (it + 1).toString() })
    }

    fun onDateClicked(day: Int) {
        val clickedDate = LocalDate.of(year, month, day)
        selectedDay.value = clickedDate

        if (accessToken != null) {

            viewModel.fetchAttendance(stdID, clickedDate.toString(), accessToken =accessToken )
        }

    }


    Log.d("ATTENDANCE-DETAILS",errorMessage.toString())
//
//    // Use this instead
//    attendanceList?.let {
//        attendanceStatus = when (it.present) {
//            1 -> "Present"
//            0 -> "Absent"
//            else -> "Not Marked"
//        }
//        attendanceTime = it.created_at ?: ""
//    } ?: run {
//        attendanceStatus = "Not Marked"
//        attendanceTime = ""
//    }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {


        // Directly derive attendanceStatus and attendanceTime
        val attendanceStatus = when {
            attendanceList == null -> "Not Marked"
            attendanceList!!.present == 1 -> "Present"
            attendanceList!!.present == 0 -> "Absent"
            else -> "Not Marked"
        }

        val attendanceTime = attendanceList?.created_at ?: ""



        // ✅ Header with Student info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            image.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Student Image",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            } ?: Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("N/A", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = studentName,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Attendance Overview",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ Today's Attendance Card
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Today's Attendance",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                when (attendanceStatus) {
                                    "Present" -> Color(0xFF4CAF50)
                                    "Absent" -> Color(0xFFE53935)
                                    else -> Color.Gray
                                },
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = attendanceStatus,
                        style = MaterialTheme.typography.body1,
                        color = when (attendanceStatus) {
                            "Present" -> Color(0xFF4CAF50)
                            "Absent" -> Color(0xFFE53935)
                            else -> Color.Gray
                        }
                    )
                }

                if (attendanceTime.isNotBlank()) {
                    Text(
                        text = attendanceTime,
                        style = MaterialTheme.typography.body2,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Calendar Header
        Text(
            text = "Attendance Summary",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { selectedDate.value = currentDate.minusMonths(1) }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
            }
            Text(
                text = "$monthName $year",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = { selectedDate.value = currentDate.plusMonths(1) }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Day labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DayOfWeek.values().forEach {
                Text(
                    text = it.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Calendar Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days) { day ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                day.isEmpty() -> Color.Transparent
                                selectedDay.value.dayOfMonth.toString() == day &&
                                        selectedDay.value.month == month &&
                                        selectedDay.value.year == year -> Color(0xFF90CAF9)
                                LocalDate.now().dayOfMonth.toString() == day &&
                                        LocalDate.now().month == month &&
                                        LocalDate.now().year == year -> Color(0xFFE0E0E0)
                                else -> Color.White
                            }
                        )
                        .clickable { if (day.isNotEmpty()) onDateClicked(day.toInt()) },
                    contentAlignment = Alignment.Center
                ) {
                    if (day.isNotEmpty()) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = if (selectedDay.value.dayOfMonth.toString() == day) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ✅ Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                "Present" to Color(0xFF4CAF50),
                "Absent" to Color(0xFFE53935),
                "Late" to Color(0xFFFFC107)
            ).forEach { (label, color) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = label, style = MaterialTheme.typography.caption)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Loading
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // ✅ Error
        errorMessage?.let {
            if (it.isNotBlank()) {
                showError = true
            }
        }

        // Show the dialog
        if (showError) {
            errorMessage?.let {
                AutoDismissAttendanceErrorDialog(
                    errorMessage = it,
                    durationMillis = 2000,
                    onDismiss = { showError = false }
                )
            }
        }


    }
}
