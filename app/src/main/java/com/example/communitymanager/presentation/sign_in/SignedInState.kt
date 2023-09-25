package com.example.communitymanager.presentation.sign_in

data class SignedInState(
    val isSignInSuccessful : Boolean = false,
    val signInError: String? = null
)
