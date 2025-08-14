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
import androidx.compose.material.Text
import androidx.compose.ui.graphics.ImageBitmap
import com.example.myapplication.viewModels.StudentDetailsViewModel



@OptIn(ExperimentalEncodingApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceScreen(studentDetailsViewModel: StudentDetailsViewModel,
    studentName: String,
    stdID: Int,
    viewModel: AttendanceViewModel = viewModel()
) {
    val attendanceList by viewModel.attendance.observeAsState()
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage.observeAsState()
    val context = LocalContext.current
    val secShared = SecurePrefsManager.getEmail(context)
    val userName = studentName
    val image = studentDetailsViewModel.studentImage.observeAsState()

    var attendanceStatus by remember { mutableStateOf("Not Marked") }
    var attendanceTime by remember { mutableStateOf("") }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) } // controls calendar month
    val selectedDay = remember { mutableStateOf(LocalDate.now()) }  // full selected day

    val currentDate = selectedDate.value
    val year = currentDate.year
    val month = currentDate.month
    val monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.uppercase() }

    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val daysInMonth = month.length(currentDate.isLeapYear)
    val emptyCells = firstDayOfMonth.dayOfWeek.ordinal
    val days = mutableListOf<String>().apply {

        addAll(List(emptyCells) { "" })
        addAll(List(daysInMonth) { (it + 1).toString() })

    }


    fun onDateClicked(day: Int) {
        val clickedDate = LocalDate.of(year, month, day)
        selectedDay.value = clickedDate
        viewModel.fetchAttendance(stdID, clickedDate.toString())
    }

    LaunchedEffect(attendanceList, selectedDay.value) {
        if (attendanceList != null) {
            attendanceStatus = if (attendanceList!!.present == 1) "Present" else "Absent"
            attendanceTime = attendanceList!!.created_at ?: ""
        } else {
            attendanceStatus = "Not Marked"
            attendanceTime = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            image.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Student Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

            } ?: Text("Invalid Image")


        }
        Spacer(modifier = Modifier.width(8.dp))


        Column {
            Text(text = userName, style = MaterialTheme.typography.h6)
            Text(text = secShared.toString(), style = MaterialTheme.typography.body2)
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Attendance Summary
        Text(
            text = "Today's Attendance",
            style = MaterialTheme.typography.body1
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Green, shape = CircleShape)
            )
            Text(
                text = attendanceStatus,
                style = MaterialTheme.typography.h6,
                color = Color.Green
            )
        }

        if (attendanceTime.isNotBlank()) {
            Text(
                text = attendanceTime,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Calendar Header
        Text(
            text = "Attendance Summary",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCCCCCC))
                .padding(5.dp),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                selectedDate.value = currentDate.minusMonths(1)
            }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
            }
            Text(text = "$monthName $year", style = MaterialTheme.typography.h6)
            IconButton(onClick = {
                selectedDate.value = currentDate.plusMonths(1)
            }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayOfWeek.values().forEach {
                Text(
                    text = it.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days) { day ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(
                            when {
                                day.isEmpty() -> Color.Transparent
                                selectedDay.value.dayOfMonth.toString() == day &&
                                        selectedDay.value.month == month &&
                                        selectedDay.value.year == year -> Color.Cyan

                                LocalDate.now().dayOfMonth.toString() == day &&
                                        LocalDate.now().month == month &&
                                        LocalDate.now().year == year -> Color.LightGray

                                else -> Color.Gray
                            },
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable {
                            if (day.isNotEmpty()) {
                                onDateClicked(day.toInt())
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (day.isNotEmpty()) {
                        Text(text = day)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(
                "Present" to Color.Green,
                "Absent" to Color.Red,
                "Late" to Color.Yellow
            ).forEach { (label, color) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading Indicator
        if (isLoading) {
            CircularProgressIndicator()
        }

        // Error message (optional)
        errorMessage?.let {
            if (it.isNotBlank()) {
                attendanceStatus = "Not Marked"
            }
        }
    }
}





