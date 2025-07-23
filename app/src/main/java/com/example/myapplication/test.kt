package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResponseBody(

    val id:String,
    val name:String
)


//
//
//@Composable
//fun SplashScreen(){
//
//    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
//
//        Image(painter = painterResource(R.drawable.camseclogo) , contentDescription = "camsec logo", modifier = Modifier.size(120.dp))
//
//        Spacer(modifier = Modifier.padding(10.dp))
//
//        Text("MITIGATE MANUAL MONITORING", fontSize = 15.sp,     fontWeight = FontWeight.Bold, letterSpacing = TextUnit(value = .2f, type = TextUnitType.Sp))
//
//    }
//
//}



//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ShowDemo(){
//
//    TestUI()
//
//
//}