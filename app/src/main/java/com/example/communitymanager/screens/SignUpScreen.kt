package com.example.communitymanager.screens

import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.communitymanager.ui.theme.CommunityManagerTheme

@Composable
fun SignUpScreen(
    navController: NavController,
    name: String,
    email: String,
    password: String,
    nameChange: (String) -> Unit,
    emailChange: (String) -> Unit,
    passChange: (String) -> Unit,
    onSignInClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .shadow(elevation = 2.dp),
        ){
        backButton( onClick = {navController.popBackStack()} , description = "back to first screen")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {


            Spacer(modifier = Modifier.weight(.4f))
            Text(
                text = "Create\nAccount",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.weight(.4f))
            outlinedTextField(
                content = name,
                onValueChange = nameChange,
                label = "Name",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.fillMaxHeight(.025f))
            outlinedTextField(
                content = email,
                onValueChange = emailChange,
                label = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.fillMaxHeight(.025f))
            outlinedTextField(
                content = password,
                onValueChange = passChange,
                label = "Password",
                modifier = Modifier.fillMaxWidth(),
                vt = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.fillMaxHeight(.05f))
            Button(
                onClick = onSignInClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Sign Up", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Already have an Account? Log in",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            navController.navigate(Screen.LoginScreen.route) {
                                popUpTo(route = Screen.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        },
                    color = MaterialTheme.colorScheme.onBackground,

                    )

            }
        }
        }

    }



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignUpScreenPreview() {
    CommunityManagerTheme {
        Surface {
        SignUpScreen(
            navController = rememberNavController(),
            name = "",
            email = "",
            password = "",
            {},
            {},
            {},{})

        }
        }
    }
