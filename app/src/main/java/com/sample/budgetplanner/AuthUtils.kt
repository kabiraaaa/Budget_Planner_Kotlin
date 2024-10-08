package com.sample.budgetplanner

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

object AuthUtils {
    private var googleApiClient: GoogleApiClient? = null

    fun getGoogleSignInIntent(activity: FragmentActivity): Intent {
        if (googleApiClient == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, null) // Ensure only one GoogleApiClient
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        }

        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
    }

    fun firebaseAuthWithGoogle(context: Context, acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                authResult.user?.let { authUser ->
                    authResult.additionalUserInfo?.let { additionalUserInfo ->
                        // Handle user information or navigate to another activity
                    }
                }
            }
    }
}
