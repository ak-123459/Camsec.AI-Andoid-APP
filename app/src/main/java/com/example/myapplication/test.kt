import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.launch

// This is the main Composable function for the app.
// It sets up the Scaffold and handles the bottom sheet state.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Remember the state of the bottom sheet, allowing it to be controlled.
    val sheetState = rememberModalBottomSheetState()
    // Remember a CoroutineScope to launch suspend functions (like showing/hiding the sheet).
    val scope = rememberCoroutineScope()
    // A state variable to track if the bottom sheet is currently visible.
    var showBottomSheet by remember { mutableStateOf(false) }

    // Sample data to be displayed in the bottom sheet.
    val attendanceData = AttendanceDetails(
        name = "Alex Johnson",
        imageUrl = "https://placehold.co/150x150/1e90ff/ffffff?text=Alex", // In a real app, use a real URL or resource ID.
        datetime = "2023-10-27 at 10:30 AM",
        status = "Present"
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Button to show the bottom sheet.
            Button(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Text("Show Attendance Details")
            }
        }
    }



    // ModalBottomSheet is displayed conditionally.
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                // This is called when the user taps outside the sheet.
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // The content of the bottom sheet is a separate composable for better code organization.
            AttendanceBottomSheetContent(attendanceData)

            // Spacer for bottom padding to avoid the navigation bar on some devices.
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

}

// Data class to hold the details of an attendance record.
data class AttendanceDetails(
    val name: String,
    val imageUrl: String,
    val datetime: String,
    val status: String)




// This Composable displays the content inside the bottom sheet.
@Composable
fun AttendanceBottomSheetContent(details: AttendanceDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Draggable handle at the top.
        // In Material 3, a drag handle is often automatically included, but you can add your own.
        // Let's add a simple one for visual consistency with the previous example.
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
            )
        }

        // The main content card for attendance details.
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image with a ring.
                Image(
                    painter = painterResource(id = R.drawable.kidsprofile), // Use a real image resource here.
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Text Details Column.
                Column {
                    Text(
                        text = details.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Status and date
                    Text(
                        text = "Status: ${details.status}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = when (details.status) {
                            "Present" -> Color.Green.copy(alpha = 0.8f) // Green for present
                            "Absent" -> Color.Red.copy(alpha = 0.8f) // Red for absent
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Date: ${details.datetime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun SHOWUI(){

    App()

}
