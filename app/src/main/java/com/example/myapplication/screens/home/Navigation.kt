import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.DemoFaceDetailsViewModel
import com.example.myapplication.FaceDetailsViewModel
import com.example.myapplication.screens.home.dashboard.DashboardScreen
import com.example.myapplication.screens.home.profile.ProfileScreen
import com.example.myapplication.screens.home.settings.SettingsScreen


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen() }
        composable("profile") { ProfileScreen() }
        composable("settings") { SettingsScreen() }
    }
}
