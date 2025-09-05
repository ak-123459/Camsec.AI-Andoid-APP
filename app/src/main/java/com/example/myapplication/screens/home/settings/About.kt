package com.example.myapplication.screens.home.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.myapplication.R
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About CAMSECAI",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3C72))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Logo/Icon
            Icon(
                painter = painterResource(R.drawable.camseclogo),
                contentDescription = "App Icon",
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = "CAMSECAI",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF6200EE)
            )

            Text(
                text = "Powered by TDBPL",
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Details
            SectionCard(
                icon = Icons.Default.Language,
                title = "Made In",
                description = "India"
            )

            SectionCard(
                icon = Icons.Default.Public,
                title = "Website",
                description = "https://tdbpl.com/",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://tdbpl.com/".toUri())
                    context.startActivity(intent)
                }
            )

            SectionCard(
                icon = Icons.Default.Notifications,
                title = "Purpose",
                description = "To provide real-time attendance details and notifications to parents and guardians."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "© 2025 TDBPL. All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionCard(icon: ImageVector, title: String, description: String, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick ?: {}
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
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
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

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}
