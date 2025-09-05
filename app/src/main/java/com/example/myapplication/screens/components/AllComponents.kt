package com.example.myapplication.screens.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerDefaults.backgroundColor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.network.DemoFaceDetails
import com.example.myapplication.R
import com.example.myapplication.network.StudentDetails
import com.example.myapplication.local.repository.decodeBase64ToBitmap
import kotlinx.coroutines.delay
import java.time.LocalDate


@Composable
fun StudentItems(face: StudentDetails, onClick: () -> Unit) {

    val croppedBitmap = remember(face.image) {
        try {
            face.image?.let { decodeBase64ToBitmap(it) }
        } catch (e: Exception) {
            Log.e("Image Decode Error", "Failed to decode base64 image", e)
            null
        }
    }

    // Card optimized for horizontal LazyRow
    androidx.compose.material3.Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .width(200.dp) // fixed width for horizontal scrolling
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0E0FF), Color(0xFFF0F0FF))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Profile image
            croppedBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Student Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    "No Image",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Student Name
            face.full_name?.let {
                androidx.compose.material3.Text(
                    text = it,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF333333),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Class & Roll
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                face.class_id?.let {
                    androidx.compose.material3.Text(
                        text = "Class: $it",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

            }
        }
    }
}









@Composable
fun DemoFaceItem(face: DemoFaceDetails, modifier: Modifier =Modifier, onClick: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        modifier = Modifier.height(100.dp)
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 6.dp).clickable {

                onClick()
            }
    ) {
        Row(
            modifier = Modifier.height(100.dp)
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

                Image(
                    painter = painterResource(face.cropped_face),
                    contentDescription = "Cropped Face",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )


            Spacer(modifier = Modifier.width(12.dp))

            // Main content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = face.student_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = face.camera_name,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
                Text(
                    text = face.recognition_time,
                    fontSize = 12.sp,
                    color = Color.Blue
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Original face and date
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                    Image(
                        painter = painterResource(face.original_face),
                        contentDescription = "Original Face",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )


                Text(
                    text = face.date,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}





val dummyFaceDetailsList = listOf(


    DemoFaceDetails(
        id = 1,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face1,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),

    DemoFaceDetails(
        id = 2,
        student_id = 102,
        student_name = "Priya Sharma",
        camera_location = "Library",
        camera_name = "Cam_02",
        cropped_face = R.drawable.face10,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:45:12"
    ),

    DemoFaceDetails(
        id = 3,
        student_id = 103,
        student_name = "Aman Verma",
        camera_location = "Cafeteria",
        camera_name = "Cam_03",
        cropped_face = R.drawable.face3,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "13:00:25"
    ),




    DemoFaceDetails(
        id = 4,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face9,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),

    DemoFaceDetails(
        id = 5,
        student_id = 102,
        student_name = "Priya Sharma",
        camera_location = "Library",
        camera_name = "Cam_02",
        cropped_face = R.drawable.face2,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:45:12"
    ),

    DemoFaceDetails(
        id = 6,
        student_id = 103,
        student_name = "Aman Verma",
        camera_location = "Cafeteria",
        camera_name = "Cam_03",
        cropped_face = R.drawable.face3,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "13:00:25"
    ),

    DemoFaceDetails(
        id = 7,
        student_id = 103,
        student_name = "Aman Verma",
        camera_location = "Cafeteria",
        camera_name = "Cam_03",
        cropped_face = R.drawable.face3,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "13:00:25"
    ),

    DemoFaceDetails(
        id = 8,
        student_id = 103,
        student_name = "Aman Verma",
        camera_location = "Cafeteria",
        camera_name = "Cam_03",
        cropped_face = R.drawable.face3,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "13:00:25"
    ),

    DemoFaceDetails(
        id = 9,
        student_id = 103,
        student_name = "Aman Verma",
        camera_location = "Cafeteria",
        camera_name = "Cam_03",
        cropped_face = R.drawable.face3,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "13:00:25"
    ),
    DemoFaceDetails(
        id = 10,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face1,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),

    DemoFaceDetails(
        id = 11,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face1,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),

    DemoFaceDetails(
        id = 12,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face1,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),
)




//
//@Composable
//fun FaceList(faces: List<DemoFaceDetails>) {
//    LazyColumn {
//        items(faces) { face ->
//            DemoFaceItem(face)
//        }
//    }
//}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewList(){

    FaceDetailBottomSheet(
        DemoFaceDetails(
        id = 1,
        student_id = 101,
        student_name = "Ravi Kumar",
        camera_location = "Main Gate",
        camera_name = "Cam_01",
        cropped_face = R.drawable.face1,
        date = "2025-07-22",
        registration_face = R.drawable.face3,
        original_face = R.drawable.office1,
        recognition_time = "12:30:45"
    ),
    )

}


@Composable
fun FaceDetailBottomSheet(face: DemoFaceDetails) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row (horizontalArrangement = Arrangement.SpaceEvenly , modifier = Modifier.fillMaxWidth() ){

            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(face.cropped_face),
                    contentDescription = "Cropped Face",
                    modifier = Modifier.padding(top = 30.dp)
                        .size(85.dp)
                        .clip(RoundedCornerShape(5.dp)
                        ), contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.size(20.dp))
            Column() {

                Text("Details", style = MaterialTheme.typography.h6, color = Color.Blue)
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text("Student Name: ${face.student_name}", modifier = Modifier.padding(2.dp))
                Text("Camera: ${face.camera_name}", modifier = Modifier.padding(2.dp))
                Text("Recognition Time: ${face.recognition_time}", modifier = Modifier.padding(2.dp))
                Text("Date: ${face.date}", modifier = Modifier.padding(2.dp))
                Spacer(Modifier.height(12.dp))

            }


        }



    }
}





@Composable
fun AppDetailsBottomSheet(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp)
    ) {
        // Close button
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Close",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "App Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Description
        Text(
            text = "This app provides detailed insights and functionalities to track attendance, manage profiles, and much more. It's designed to help users track important data with an intuitive interface.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App version
        Text(
            text = "Version: 1.0.0",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Developer Info
        Text(
            text = "Developed by: Camsec.AI Team",
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // More information or links
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
            ) {
                Text(text = "Ok", color = Color.White)
            }
        }
    }
}


@Composable
fun ModernCircularLoader(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    strokeWidth: Dp = 6.dp,
    color: Color = Color(0xFF42A5F5)
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth
    )
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        ModernCircularLoader()
    }
}


@Composable
fun GradientCircularSpinner(
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
    colors: List<Color> = listOf(Color(0xFF42A5F5), Color(0xFF478ED1)),
    animationDuration: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinner-rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }

    Canvas(
        modifier = Modifier
            .size(size)
            .rotate(angle)
    ) {
        val diameter = size
        val radius = diameter / 2
        val sweepAngle = 270f

        drawArc(
            brush = Brush.sweepGradient(colors),
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun GradientSpinnerDemo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        GradientCircularSpinner()
    }
}




@Preview
@Composable
fun ShowPreview3(){


    LoadingToggleScreen()

}



@Composable
fun LoadingToggleScreen() {
    // This keeps track of whether to show or hide the loader
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tap the button to toggle loading", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { isLoading = !isLoading }) {
                Text(if (isLoading) "Hide Progress" else "Show Progress")
            }
        }

        // Loading overlay with progress bar
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
//                    .clickable(enabled = true) {}, // Blocks interaction with background
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun CustomMessageDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    message: String,
    icon: ImageVector? = null,
    confirmButtonText: String? = null,
    onConfirm: (() -> Unit)? = null,
    dismissButtonText: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    // State to control the visibility of the dialog content for animation
    var animateVisibility by remember { mutableStateOf(false) }

    if (showDialog.value) {
        // Use Dialog composable for full customization
        Dialog(
            onDismissRequest = {
                animateVisibility = false // Trigger exit animation
                // Delay dismissal to allow animation to complete
                // This requires a LaunchedEffect with a delay in a real app
                // For simplicity, we'll dismiss immediately after animation state change
                showDialog.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false, // Allow custom width
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ) {
            // AnimatedVisibility for entry and exit animations
            AnimatedVisibility(
                visible = animateVisibility,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                        scaleIn(animationSpec = tween(durationMillis = 300), initialScale = 0.8f),
                exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                        scaleOut(animationSpec = tween(durationMillis = 300), targetScale = 0.8f)
            ) {
                // Card provides a modern, elevated look for the dialog
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f) // Set custom width
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp), // Rounded corners
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Optional Icon
                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = "Dialog Icon",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.height(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Title
                        Text(
                            text = title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Message
                        Text(
                            text = message,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            dismissButtonText?.let {
                                TextButton(onClick = {
                                    animateVisibility = false
                                    showDialog.value = false
                                    onDismiss?.invoke()
                                }) {
                                    Text(it, color = MaterialTheme.colors.secondary)
                                }
                            }

                            confirmButtonText?.let {
                                Button(onClick = {
                                    animateVisibility = false
                                    showDialog.value = false
                                    onConfirm?.invoke()
                                }) {
                                    Text(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AdvancedBlackProgressDialog(onDismissRequest: () -> Unit ) {
    // Defines the animation for the infinite rotation of the progress bar.
    val infiniteTransition = rememberInfiniteTransition(label = "rotationTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    // A dialog with properties that prevent it from being dismissed on back press or outside clicks.
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {

        Card(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            backgroundColor = Color.Black
        )
        {
            Box(
                modifier = Modifier
                    .size(150.dp) // The size of the dialog content area.
                    .padding(24.dp), // Padding inside the card.
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(rotation),
                    color = Color.White,
                    backgroundColor = Color.DarkGray,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

@Composable
fun AutoDismissAttendanceErrorDialog(
    errorMessage: String,
    durationMillis: Long = 500, // default half a second
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    // Automatically dismiss after duration
    LaunchedEffect(key1 = errorMessage) {
        if (visible) {
            delay(durationMillis)
            visible = false
            onDismiss()
        }
    }

    if (visible) {
        AlertDialog(
            onDismissRequest = {
                visible = false
                onDismiss()
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Attendance Error",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Text(
                    text = errorMessage,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    visible = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }
}


@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {

    var startAnimation by remember { mutableStateOf(false) }

    // Animate alpha and scale
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000) // Splash screen duration
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main content centered
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(alphaAnim)
                .scale(scaleAnim)
        ) {
            Image(
                painter = painterResource(R.drawable.camseclogo),
                contentDescription = "camsec logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "MITIGATE  MANUAL  MONITORING",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = TextUnit(0.2f, TextUnitType.Sp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Powered by TDBPL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Bottom text anchored at the bottom
        Text(
            "Made in India",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(alphaAnim)
                .scale(scaleAnim)
        )
    }
}

/// Data class (unchanged)


/// Data class for attendance details (no changes)
data class AttendanceDetails(
    val name: String?,
    val imageUrl: Bitmap?,
    val datetime: String?,
    val status: String?,
    val error: String?
)





@Composable
fun AttendanceBottomSheetContent(details: AttendanceDetails?) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var showFullImage by remember { mutableStateOf(false) } // 👈 state for full-screen image

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight * 0.8f) // 👈 80% of screen height
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colors.surface)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.secondaryVariant.copy(alpha = 0.6f))
        )

        if (details == null) {
            // Show loader if details not available
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 4.dp
                )
            }
        } else {
            // ==== Main content when details available ====
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // scroll if content is large
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Rectangular image with click to expand
                Image(
                    painter = (details.imageUrl?.asImageBitmap()?.let { BitmapPainter(it) }
                        ?: painterResource(id = R.drawable.kidsprofile)) as Painter,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showFullImage = true }, // 👈 open full screen
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Student Name (big)
                details.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h5.copy(fontSize = 22.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Status badge (smaller size, pastel)
                details.status?.let {
                    Text(
                        text = it.uppercase(),
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                color = when (details.status) {
                                    "Present" -> Color(0xFFA5D6A7) // soft green
                                    "Absent" -> Color(0xFFEF9A9A) // soft red
                                    else -> Color.LightGray
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Divider(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    color = MaterialTheme.colors.surface.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date & Time (smaller text)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Date",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    details.datetime?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }

                // Error message (smaller + softer)
                details.error?.takeIf { it.isNotBlank() }?.let { errorMsg ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colors.error.copy(alpha = 0.1f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colors.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = errorMsg,
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Normal),
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    }

    // ====== Full-Screen Image Preview ======
    if (showFullImage) {
        Dialog(onDismissRequest = { showFullImage = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = (details?.imageUrl?.asImageBitmap()?.let { BitmapPainter(it) }
                        ?: painterResource(id = R.drawable.kidsprofile)) as Painter,
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { showFullImage = false }, // tap again to close
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}



