//package com.example.communitymanager
//
//import android.app.Activity.RESULT_OK
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCompositionContext
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.LifecycleCoroutineScope
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//import androidx.navigation.NavController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.communitymanager.presentation.sign_in.GoogleAuthUiClient
//import com.example.communitymanager.presentation.sign_in.SignInViewModel
//import com.example.communitymanager.presentation.sign_in.SignedInState
//import com.example.communitymanager.screens.FirstScreen
//import com.example.communitymanager.screens.LoginScreen
//
//import com.example.communitymanager.screens.Screen
//import com.example.communitymanager.screens.SignUpScreen
//import kotlinx.coroutines.launch
//
//
//@Composable
//fun Navigation(googleAuthUiClient: GoogleAuthUiClient , lifecyclescope: LifecycleCoroutineScope) {
//    val navController = rememberNavController()
//
//
//
//    NavHost(navController = navController, startDestination = Screen.FirstScreen.route ){
//        composable(route = Screen.FirstScreen.route){
//            val viewModifier = viewModel< SignInViewModel >()
//            val state by viewModifier.state.collectAsStateWithLifecycle()
//            val coroutineScope = rememberCoroutineScope()
//            val launcher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartIntentSenderForResult(),
//                onResult = {
//                    result ->
//                    if (result.resultCode == RESULT_OK){
//                        lifecyclescope.launch{
//                            val signInResult = googleAuthUiClient.SignInWithIntent(
//                                intent = result.data ?: return@launch
//                            )
//                            viewModifier.onSignInResult(signInResult)
//                        }
//                    }
//                }
//            )
//           FirstScreen(
//               navController = navController ,
//               state =state ,
//               onSignInClick = {
//               lifecyclescope.launch {
//                   val signInIntent = googleAuthUiClient.signIn()
//                   launcher.launch(IntentSenderRequest.Builder(
//                       signInIntent ?: return@launch
//                   ))
//               }
//           })
//        }
//        composable(route = Screen.MainScreen.route){
//            MainScreen(navController = navController)
//        }
//        composable(route = Screen.DetailScreen.route ){
//            DetailScreen()
//        }
//        composable(route = Screen.LoginScreen.route){
//            LoginScreen(navController)
//        }
//        composable(route = Screen.SignUpScreen.route){
//            SignUpScreen(navController)
//        }
//
//    }
//
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen(navController: NavController) {
//  var text by remember {
//      mutableStateOf("")
//  }
//    Column(
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 50.dp)
//    ) {
//        TextField(value = text, onValueChange ={text = it} )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(
//            onClick = { navController.navigate(Screen.DetailScreen.route)},
//            modifier = Modifier.align(Alignment.End)) {
//            Text(text = "To Detail Screen")
//        }
//    }
//}
//
//@Composable
//fun DetailScreen() {
//    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
//        Text(text = "Hello ")
//    }
//}
