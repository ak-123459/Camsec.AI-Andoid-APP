package com.example.myapplication


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.FaceDetailsViewModel
import com.example.myapplication.screens.home.MyApp
import kotlinx.coroutines.delay

enum class LaunchScreens{

    HOME,SPLASH
}



class HomeActivity : ComponentActivity() {


    private lateinit var viewModel: DemoFaceDetailsViewModel
//    private lateinit var viewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize the ViewModel
        viewModel = ViewModelProvider(this)[DemoFaceDetailsViewModel::class.java]

        setContent {


            AppEntryPoint()

            }

    }
}


@Composable
fun AppEntryPoint() {

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        showSplash = false
    }

    if (showSplash) {

        SplashScreen()

    } else {

        MyApp()
    }
}



@Composable
fun SplashScreen(){

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {

        Image(painter = painterResource(R.drawable.camseclogo) , contentDescription = "camsec logo", modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.padding(10.dp))

        Text("MITIGATE  MANUAL  MONITORING", fontSize = 15.sp,     fontWeight = FontWeight.Bold, letterSpacing = TextUnit(value = .2f, type = TextUnitType.Sp))

    }

}




//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}