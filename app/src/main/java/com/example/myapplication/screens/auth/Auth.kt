package com.example.myapplication.screens.auth


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.HomeActivity
import com.example.myapplication.R
import com.example.myapplication.firebase.getFCMToken
import com.example.myapplication.network.LoginRequestData
import com.example.myapplication.screens.components.AdvancedBlackProgressDialog
import com.example.myapplication.utility.SecurePrefsManager
import com.example.myapplication.viewModels.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun AuthAppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }

        // Add other routes if needed
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val loginResult by viewModel.loginResult.observeAsState()
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading
    var showDialog by remember { mutableStateOf(false) }

    // Remember variables for user input
    var parentCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val secPref = SecurePrefsManager
    val context = LocalContext.current
    val activity = context as? Activity
    var fcmToken by remember { mutableStateOf("") }

    // 👇 NEW: State to manage password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    // 👇 NEW: Focus requesters for keyboard navigation
    val (emailFocusRequester, passwordFocusRequester) = remember { FocusRequester.createRefs() }

    // 👇 NEW: Scroll state for auto-scrolling
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // 👇 NEW: Keyboard controller to hide the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    // Call only once when the screen is first composed
    LaunchedEffect(Unit) {
        getFCMToken { token ->
            fcmToken = token
        }

    }



    // Set a dark theme for the screen
    MaterialTheme(
        colors = darkColors(
            primary = Color.Black,
            onPrimary = Color.White,
            surface = Color.Black,
            onSurface = Color.White
        )
    ) {



            Box(
                modifier = Modifier
                    .background(Color.White)
            ) {

                // Main content of the screen

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF2F2F2))
                        .padding(WindowInsets.systemBars.asPaddingValues()) // Safe area for status/navigation bars
                ) {


                // Logo at the top
                Image(
                    painter = painterResource(id = R.drawable.camseclogo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp, bottom = 30.dp)
                        .size(120.dp)
                )

                Spacer(modifier = Modifier.padding(40.dp))

                // Bottom card view with login form
                // 👇 NEW: Add verticalScroll modifier
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .background(Color.White)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .padding(5.dp)
                        , verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.h5.copy(color = Color.Black),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email input field
                    OutlinedTextField(
                        maxLines = 1,
                        value = parentCode,
                        onValueChange = { parentCode = it },
                        label = { Text("Parent Code", color = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            // 👇 NEW: Attach focus requester for keyboard navigation
                            .focusRequester(emailFocusRequester)
                            // 👇 NEW: Auto-scroll when focused
                            .onGloballyPositioned { coordinates ->
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(coordinates.positionInRoot().y.toInt())
                                }
                            },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Black
                        ),
                        isError = errorMessage.value.isNotEmpty(),
                        // 👇 NEW: Configure keyboard options for "Next" button
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        // 👇 NEW: Define action for "Next" button
                        keyboardActions = KeyboardActions(
                            onNext = {
                                passwordFocusRequester.requestFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input field
                    OutlinedTextField(
                        maxLines = 1,
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            // 👇 NEW: Attach focus requester for keyboard navigation
                            .focusRequester(passwordFocusRequester)
                            // 👇 NEW: Auto-scroll when focused
                            .onGloballyPositioned { coordinates ->
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(coordinates.positionInRoot().y.toInt())
                                }
                            },
                        // 👇 NEW: Toggle password visibility
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Black
                        ),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff
                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        },
                        isError = errorMessage.value.isNotEmpty(),
                        // 👇 NEW: Configure keyboard options for "Done" button
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        // 👇 NEW: Define action for "Done" button (login)
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()

                                login(
                                    parentCode,
                                    password,
                                    viewModel,
                                    errorMessage,
                                    secPref,
                                    context,
                                    activity, fcmToken
                                )


                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display error message if any
                    if (errorMessage.value.isNotEmpty()) {
                        Text(
                            text = errorMessage.value,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Login Button
                    Button(
                        onClick = {
                            keyboardController?.hide()


                            login(
                                parentCode,
                                password,
                                viewModel,
                                errorMessage,
                                secPref,
                                context,
                                activity, fcmToken
                            )

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text("Login", color = Color.White)
                    }


                }


            }

        }
    }

    // Conditionally show the dialog.
    if (isLoading) {
        AdvancedBlackProgressDialog {
            // When the dialog is dismissed, hide it by updating the state.
            showDialog = false
        }
    }

    Log.d("Token----",fcmToken)

}

private fun login(
    parentCode: String,
    password: String,
    viewModel: LoginViewModel,
    errorMessage: MutableState<String>,
    secPref: SecurePrefsManager,
    context: Context,
    activity: Activity?,
    fcmToken: String
) {
    if (parentCode.isBlank() || password.isBlank()) {
        errorMessage.value = "Please fill in both fields."
        return
    }

    if (fcmToken.isBlank()) {
        errorMessage.value = "FCM Token not available. Try again."
        return
    }


    Log.d("FCM-TOKEN->", fcmToken)

    // Trigger login with FCM token
    viewModel.loginUser(
        LoginRequestData(parent_code = parentCode, password_hash = password,fcmToken)
    )

    // 👇 Observe login result and navigate only on success.
    viewModel.loginResult.observeForever { user ->

        if (user != null) {
            // Save preferences
            user.access_token?.let { secPref.saveToken(context, it) }
            secPref.saveParentCode(context, parentCode)
            secPref.saveFullName(context, user.full_name ?: "")

            Log.d("LOGIN", "Login successful: ${user.full_name}")

            // Navigate to HomeActivity
            context.startActivity(Intent(context, HomeActivity::class.java))
            activity?.finish()

        }
    }

    // 👇 Also handle error from ViewModel
    viewModel.error.observeForever { error ->
        if (!error.isNullOrBlank()) {
            errorMessage.value = error
            Log.e("LOGIN_ERROR", error)
        }
    }
}



@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    MaterialTheme(
        colors = darkColors(
            primary = Color.Black,
            onPrimary = Color.White,
            surface = Color.Black,
            onSurface = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.camseclogo), // Replace with your logo resource
                contentDescription = "App Logo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
                    .size(120.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .padding(32.dp)
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.h5.copy(color = Color.Black),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(maxLines = 1,
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black
                    ),
                    isError = errorMessage.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(maxLines = 1,
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black
                    ),
                    isError = errorMessage.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(maxLines = 1,
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black
                    ),
                    isError = errorMessage.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            errorMessage = "Please fill in all fields."
                        } else if (password != confirmPassword) {
                            errorMessage = "Passwords do not match."
                        } else {
                            errorMessage = ""
                            // Handle registration logic
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("Register", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {   navController.navigate("login")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Already have an account? Login", color = Color.Black)
                }
            }
        }
    }
}







