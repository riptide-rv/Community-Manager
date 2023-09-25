package com.example.communitymanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.communitymanager.presentation.sign_in.AuthUiClient
import com.example.communitymanager.presentation.sign_in.SignInResult
import com.example.communitymanager.presentation.sign_in.SignInViewModel
import com.example.communitymanager.presentation.sign_in.SignedInState
import com.example.communitymanager.presentation.sign_in.UserData
import com.example.communitymanager.screens.FirstScreen
import com.example.communitymanager.screens.HomeScreen
import com.example.communitymanager.screens.LoginScreen
import com.example.communitymanager.screens.Screen
import com.example.communitymanager.screens.SignUpScreen
import com.example.communitymanager.ui.theme.CommunityManagerTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    
    private val googleAuthUiClient by lazy {
        AuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            CommunityManagerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.FirstScreen.route ){
                        composable(Screen.FirstScreen.route){
                            val (state, launcher) = googleSignInLauncher(navController)
                            FirstScreen(
                                navController = navController,
                                state = state ,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }

                        composable(route = Screen.LoginScreen.route){
                            val viewModel  = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            var email by remember {
                                mutableStateOf("")
                            }
                            var password by remember {
                                mutableStateOf("")
                            }
                            LaunchedEffect(key1 = state.isSignInSuccessful){
                                if(state.isSignInSuccessful){
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed in Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(Screen.HomeScreen.route)
                                }
                            }
                            LoginScreen(
                                navController =  navController,
                                email = email,
                                password = password,
                                emailChange = {email = it},
                                passChange = {password = it},
                                onLoginClick = { logInWithEmailAndPassword(email, password, viewModel) }
                            )
                        }

                        composable(route = Screen.SignUpScreen.route) {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            var name by remember {
                                mutableStateOf("")
                            }
                            var email by remember {
                                mutableStateOf("")
                            }
                            var password by remember {
                                mutableStateOf("")
                            }
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed in Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(Screen.HomeScreen.route)
                                }
                            }
                            SignUpScreen(
                                navController = navController,
                                name = name,
                                email = email,
                                password = password,
                                nameChange = {name = it},
                                emailChange = {email = it},
                                passChange = {password = it}

                            ) {
                                Firebase.auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {
                                        var signInResult: SignInResult
                                        if (it.isSuccessful) {
                                            val profileUpdates = userProfileChangeRequest {
                                                displayName = name
                                            }
                                            it.result.user!!.updateProfile(profileUpdates)
                                                .addOnCompleteListener { result ->
                                                    if (result.isSuccessful) {
                                                        val user = Firebase.auth.currentUser
                                                        signInResult = SignInResult(
                                                            data = user?.run {
                                                                UserData(
                                                                    userId = uid,
                                                                    userName = displayName,
                                                                    profilePictureUrl = photoUrl?.toString(),
                                                                    userEmail = getEmail()
                                                                )
                                                            },
                                                            errorMessage = null
                                                        )
                                                        viewModel.onSignInResult(signInResult)
                                                    } else {
                                                        signInResult = SignInResult(
                                                            null,
                                                            it.exception?.message

                                                        )
                                                    }
                                                }
                                        } else {
                                            signInResult = SignInResult(
                                                null,
                                                it.exception?.message

                                            )
                                        }

                                    }
                            }
                        }

                        composable(route = Screen.HomeScreen.route){
                           HomeScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch{
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed Out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate(Screen.FirstScreen.route){
                                            popUpTo(Screen.FirstScreen.route)
                                        }
                                    }
                                }
                            )
                        }

                    }
                }
            }
        }
    }


    private fun logInWithEmailAndPassword(
        email: String,
        password: String,
        viewModel: SignInViewModel
    ) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val signInResult: SignInResult
            if (it.isSuccessful) {
                val user = it.result.user
                signInResult = SignInResult(
                    data = user?.run {
                        UserData(
                            userId = uid,
                            userName = displayName,
                            profilePictureUrl = photoUrl?.toString(),
                            userEmail = getEmail()
                        )
                    },
                    errorMessage = null
                )
            } else {
                signInResult = SignInResult(
                    null,
                    it.exception?.message

                )
            }
            viewModel.onSignInResult(signInResult)
        }
    }


    @Composable
    private fun googleSignInLauncher(navController: NavHostController): Pair<SignedInState, ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>> {
        val viewModel = viewModel<SignInViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = Unit) {
            if (googleAuthUiClient.getSignedInUser() != null) {
                navController.navigate(Screen.HomeScreen.route)
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult =
                            googleAuthUiClient.SignInWithIntent(result.data ?: return@launch)
                        viewModel.onSignInResult(signInResult)
                    }
                }
            }
        )

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Signed in Successfully",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Screen.HomeScreen.route)
            }
        }
        return Pair(state, launcher)
    }

}



