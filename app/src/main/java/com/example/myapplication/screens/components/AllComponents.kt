package com.example.myapplication.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DemoFaceDetails
import com.example.myapplication.FaceDetails
import com.example.myapplication.R
import com.example.myapplication.local.repository.decodeBase64ToBitmap




@Composable
fun FaceItem(face: FaceDetails) {
    val croppedBitmap = remember(face.cropped_face) {
        decodeBase64ToBitmap(face.cropped_face)
    }

    val originalBitmap = remember(face.original_face) {
        decodeBase64ToBitmap(face.original_face)
    }


    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Cropped face image
            croppedBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Cropped Face",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

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
                originalBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Original Face",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

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










@Composable
fun DemoFaceItem(face: DemoFaceDetails,modifier: Modifier =Modifier,onClick: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp).clickable {

                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

                Image(
                    painter = painterResource(face.cropped_face),
                    contentDescription = "Cropped Face",
                    modifier = Modifier
                        .size(48.dp)
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

    FaceDetailBottomSheet(DemoFaceDetails(
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


