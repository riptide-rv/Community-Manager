package com.example.communitymanager.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.communitymanager.presentation.sign_in.SignedInState


@Composable
fun outlinedButton(onClick:()->Unit,content: String) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = content)
    }
}

@Composable
fun FirstScreen(navController: NavController ,state:SignedInState , onSignInClick : () -> Unit) {
    val context  = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let{ error ->
            Toast.makeText(context,error,Toast.LENGTH_LONG).show()
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 52.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            outlinedButton(
                onClick = { navController.navigate(Screen.SignUpScreen.route) },
                content = "SIGN UP")
            Spacer(modifier = Modifier.height(8.dp))
            outlinedButton(
                onClick = { navController.navigate(route = Screen.LoginScreen.route) },
                content = "LOGIN"
            )
            Spacer(modifier = Modifier.fillMaxHeight(.05f))
            Button(
                onClick = onSignInClick ,
                modifier = Modifier.fillMaxWidth() ,
                shape = MaterialTheme.shapes.medium) {
                Text(text = "Sign In with Google" , style = MaterialTheme.typography.titleMedium)
            }

        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FirstScreenPreview(){
    FirstScreen(navController = rememberNavController() , SignedInState()) {}
}

