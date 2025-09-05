package com.example.myapplication.screens.home.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsApp(navController: NavController) {
    val scrollState = rememberScrollState()
    var showAcceptanceDialog by remember { mutableStateOf(false) }
    var showFab by remember { mutableStateOf(false) }

    // Logic to show FAB only when scrolled down
    LaunchedEffect(scrollState.value) {
        showFab = scrollState.value > 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terms of Use",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3C72))
            )
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { showAcceptanceDialog = true },
                    modifier = Modifier.padding(bottom = 70.dp),
                    containerColor = Color(0xFF000000),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome to CAMSECAI, provided by TDBPL. By using this application, you agree to these Terms of Use. Please read them carefully.",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            val effectiveDate = SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(Date())
            Text(
                text = "Effective Date: $effectiveDate",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            SectionCard(
                icon = Icons.Default.Info,
                title = "1. Purpose of the App",
                description = "CAMSECAI allows parents and guardians to securely view the attendance records of their child(ren) as recorded through school CCTV and AI-based face recognition."
            )

            SectionCard(
                icon = Icons.Default.AccountCircle,
                title = "2. Account Access",
                description = "Parents must register/login using credentials provided or approved by their child’s school.\n\nYou are responsible for maintaining the confidentiality of your login details. Any activity under your account will be considered your responsibility."
            )

            SectionCard(
                icon = Icons.Default.Gavel,
                title = "3. Acceptable Use",
                description = "You agree not to:\n- Share or misuse your login credentials.\n- Attempt to access data of other students.\n- Copy, distribute, or misuse attendance data.\n- Interfere with or disrupt the app’s operation."
            )

            SectionCard(
                icon = Icons.Default.CheckCircle,
                title = "4. Data Accuracy",
                description = "Attendance data is based on automated face recognition and CCTV monitoring.\n\nWhile we use advanced AI technology, there may be rare cases of errors. Schools retain the final authority to validate and correct attendance records."
            )

            SectionCard(
                icon = Icons.Default.Lock,
                title = "5. Privacy",
                description = "Your use of the app is also governed by our Privacy Policy, which explains how we collect, use, and protect your data."
            )

            SectionCard(
                icon = Icons.Default.Warning,
                title = "6. Limitation of Liability",
                description = "CAMSECAI provides attendance records as a service to schools and parents. We are not liable for any loss, damages, or disputes arising from attendance errors, connectivity issues, or unauthorized account access. Schools remain the primary authority for attendance validation."
            )

            SectionCard(
                icon = Icons.Default.ExitToApp,
                title = "7. Termination of Access",
                description = "We may suspend or terminate your account if you misuse the app, breach these Terms, or cease to be authorized by the school."
            )

            SectionCard(
                icon = Icons.Default.Update,
                title = "8. Updates to Terms",
                description = "We may update these Terms of Use periodically. Changes will be notified through the app or our website. Continued use of the app after changes means you accept the new Terms."
            )

            SectionCard(
                icon = Icons.Default.Balance,
                title = "9. Governing Law",
                description = "These Terms shall be governed by the laws of India. Any disputes shall be subject to the jurisdiction of courts located in Kolkata."
            )

            SectionCard(
                icon = Icons.Default.ContactMail,
                title = "10. Contact Us",
                description = "For questions regarding these Terms, please contact:\nTDBPL (CAMSECAI)\nAddress: [Insert Company Address]\nEmail: info@tdbpl.com\nPhone: +91-7000258253"
            )

            // A large space to ensure the list is long enough to scroll
            Spacer(modifier = Modifier.height(300.dp))
        }

        // Acceptance Dialog
        if (showAcceptanceDialog) {
            AlertDialog(
                onDismissRequest = { /* Don't dismiss on outside click */ },
                title = {
                    Text(text = "Terms Accepted")
                },
                text = {
                    Text("Thanks for accepting the terms of use!")
                },
                confirmButton = {}
            )

            // Dismiss the dialog and navigate after 2 seconds
            LaunchedEffect(Unit) {
                delay(1500)
                showAcceptanceDialog = false

                navController.navigate("dashboard") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun SectionCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
        }
    }
}

// @Preview(showBackground = true, show
