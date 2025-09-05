package com.example.communityai
// GoogleAuthClient.kt
import android.content.Context
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context,
    private val auth: FirebaseAuth = Firebase.auth
) {
    private val credentialManager = androidx.credentials.CredentialManager.create(context)

    suspend fun signIn(): SignInResult {
        return try {
            // Create Google ID option
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(getWebClientId())
                .setFilterByAuthorizedAccounts(false)
                .build()

            // Create credential request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Get credential
            val response = credentialManager.getCredential(
                request = request,
                context = context
            )

            // Handle the credential
            handleSignIn(response)
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message ?: "Failed to get credential"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message ?: "Unknown error occurred"
            )
        }
    }

    private suspend fun handleSignIn(response: GetCredentialResponse): SignInResult {
        val credential = response.credential

        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )

                val authResult = auth.signInWithCredential(firebaseCredential).await()

                SignInResult(
                    data = authResult.user?.run {
                        UserData(
                            userId = uid,
                            username = displayName ?: "",
                            email = email ?: "",
                            profilePictureUrl = photoUrl?.toString()
                        )
                    },
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                SignInResult(
                    data = null,
                    errorMessage = e.message ?: "Failed to sign in with Google"
                )
            }
        } else {
            SignInResult(
                data = null,
                errorMessage = "Unexpected credential type"
            )
        }
    }

    suspend fun signOut() {
        try {

            auth.signOut()


            val clearRequest = androidx.credentials.ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getWebClientId(): String {

        return context.getString(R.string.default_web_client_id)
    }
}

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)