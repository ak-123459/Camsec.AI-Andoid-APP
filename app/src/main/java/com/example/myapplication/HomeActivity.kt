package com.example.myapplication


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.local.db.DatabaseProvider
import com.example.myapplication.local.db.NotificationEntity
import com.example.myapplication.screens.auth.AuthAppNavigator
import com.example.myapplication.screens.components.SplashScreen
import com.example.myapplication.screens.home.MyApp
import kotlinx.coroutines.delay
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.viewModels.StudentDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeActivity : ComponentActivity() {


    private lateinit var viewModel: StudentDetailsViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize the ViewModel
        viewModel = ViewModelProvider(this)[StudentDetailsViewModel::class.java]
        val db = DatabaseProvider.getDatabase(this)
        val screenFromIntent = intent.getStringExtra("screen")

        if(screenFromIntent=="notification"){

            // 2. Create an Intent for the target activity
            val intent = Intent(this, MainActivity::class.java)
            // 3. Start the activity using the context
            this.startActivity(intent)
            this.finish()

        }


        setContent {

            AppEntryPoint()

            }

    }
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppEntryPoint() {

    val context = LocalContext.current

    val secPref  = SecurePrefsManager.getToken(context)

    var showSplash by remember { mutableStateOf(true) }



    if (showSplash) {

        SplashScreen(onSplashFinished = {showSplash=false})
    }

    else if(!showSplash && secPref != null){

        MyApp()

    }

    else {
        AuthAppNavigator()
    }

    }





//@Composable
//fun SplashScreen(){
//
//    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
//
//        Image(painter = painterResource(R.drawable.camseclogo) , contentDescription = "camsec logo", modifier = Modifier.size(120.dp))
//
//        Spacer(modifier = Modifier.padding(10.dp))
//
//        Text("MITIGATE  MANUAL  MONITORING", fontSize = 15.sp,     fontWeight = FontWeight.Bold, letterSpacing = TextUnit(value = .2f, type = TextUnitType.Sp))
//
//    }
//
//}


