import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.screens.auth.AuthAppNavigator
import com.example.myapplication.screens.home.dashboard.DashboardScreen
import com.example.myapplication.screens.home.profile.ProfileScreen
import com.example.myapplication.screens.home.notifications.NotificationListScreen
import com.example.myapplication.screens.settings.SettingsScreen
import com.example.myapplication.screens.auth.LoginScreen
import com.example.myapplication.screens.home.profile.ProfileAppNavHost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val context = LocalContext.current.applicationContext
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen() }
        composable("profile") { ProfileAppNavHost() }
        composable("settings") { SettingsScreen(navController) }

    }


}
