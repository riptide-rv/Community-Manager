package com.example.communitymanager.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.communitymanager.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


class AuthUiClient(
    private val context: Context,
    private val oneTapClient : SignInClient ) {
    private val auth = Firebase.auth
    //google auth
    suspend fun signIn(): IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                beginSignIn()
            ).await()
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun SignInWithIntent(intent :Intent) :SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken  = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try{
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run{
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        userEmail = email
                    )
                },
                errorMessage = null
            )
        }catch(e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
            null,e.message
            )
        }
    }

    //email and password
    suspend fun createUserWithEmailPassword(intent :Intent ,email:String, password: String): SignInResult{
        return try{
            val user = auth.createUserWithEmailAndPassword(email,password).await().user
            SignInResult(
                data = user?.run{
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        userEmail = getEmail()
                    )
                },
                errorMessage = null
            )
        }catch(e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                null,e.message
            )
        }
    }

    suspend fun signInWithEmailAndPassword(email: String,password: String) :SignInResult{
        return try{
            val user = auth.signInWithEmailAndPassword(email,password).await().user
            SignInResult(
                data = user?.run{
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        userEmail = getEmail()

                    )
                },
                errorMessage = null
            )
        }catch(e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                null,e.message
            )
        }

    }

    suspend fun signOut() {
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
    fun getSignedInUser() : UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString(),
            userEmail = email
        )
    }
    private fun beginSignIn(): BeginSignInRequest{
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client))
                    .build()

            ).setAutoSelectEnabled(true)
            .build()
    }
}