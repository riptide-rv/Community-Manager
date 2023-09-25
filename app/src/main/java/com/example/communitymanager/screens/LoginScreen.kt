package com.example.communitymanager.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.communitymanager.presentation.sign_in.AuthUiClient
import com.example.communitymanager.presentation.sign_in.SignedInState
import com.example.communitymanager.ui.theme.CommunityManagerTheme
import com.google.android.gms.auth.api.identity.SignInPassword

@Composable
fun LoginScreen(navController: NavController,
                email:String,
                password: String ,
                emailChange : (String) -> Unit,
                passChange: (String) -> Unit,
                onLoginClick : () -> Unit) {
    Column(
        Modifier
            .padding(8.dp)
            .shadow(elevation = 2.dp)) {


       backButton( onClick = {navController.popBackStack()} , description = "back to first screen")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {


            Spacer(modifier = Modifier.weight(.4f))
            Text(text = "Welcome\nBack" , fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.weight(.4f))
            outlinedTextField(
                content = email,
                onValueChange =emailChange,
                label = "Email",
                modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.fillMaxHeight(.025f))
            outlinedTextField(content =password , onValueChange = passChange, label = "Password" , modifier = Modifier.fillMaxWidth() , vt = PasswordVisualTransformation() )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp) ,
                horizontalArrangement = Arrangement.End){
                Text(text = "Forgot password?" , style = MaterialTheme.typography.bodyMedium , modifier = Modifier.wrapContentSize())}
            Spacer(modifier = Modifier.fillMaxHeight(.05f))
            Button(onClick = onLoginClick , modifier = Modifier.fillMaxWidth() ,shape = MaterialTheme.shapes.medium) {
                Text(text = "Log in" , style = MaterialTheme.typography.titleMedium)
            }


            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp) ,
                horizontalArrangement = Arrangement.SpaceAround){
                Text(
                    text = "Dont have an Account? Sign up" ,
                    style = MaterialTheme.typography.bodyMedium ,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            navController.navigate(Screen.SignUpScreen.route) {
                                popUpTo(route = Screen.SignUpScreen.route) {
                                    inclusive = true
                                }
                            }
                        } ,
                    color = MaterialTheme.colorScheme.onBackground,

                )

                    }

        }



    }

}

@Composable
fun backButton(onClick : () ->Unit, description :String) {
    Row(Modifier.padding(4.dp)) {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.ArrowBack, contentDescription = description)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun outlinedTextField(content:String,onValueChange:(String)->Unit , label: String , modifier : Modifier , vt : VisualTransformation = VisualTransformation.None) {
    OutlinedTextField(
        value = content,
        onValueChange = onValueChange ,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = vt,
        shape = MaterialTheme.shapes.medium
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview() {
    CommunityManagerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen(
                navController = rememberNavController(),
                email ="",
                password = "",
                emailChange = {},
                passChange = {},
                onLoginClick = {}
            )
        }
    }

}