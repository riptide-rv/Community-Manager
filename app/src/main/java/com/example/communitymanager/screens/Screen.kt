package com.example.communitymanager.screens

sealed class Screen(val route:String){

    object FirstScreen : Screen("first_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object HomeScreen : Screen("home_screen")

    fun withArgs(vararg args:String): String{
        return buildString {
            append(route)
            args.forEach { append("/$it") }
        }
    }
}
