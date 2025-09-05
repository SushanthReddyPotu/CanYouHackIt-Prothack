package com.example.communityai


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private lateinit var googleAuthClient: GoogleAuthClient

    fun initialize(context: Context) {
        googleAuthClient = GoogleAuthClient(context)
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        _isUserLoggedIn.value = currentUser != null
        currentUser?.let { user ->
            _userData.value = UserData(
                userId = user.uid,
                username = user.displayName ?: "",
                email = user.email ?: "",
                profilePictureUrl = user.photoUrl?.toString()
            )
        }
    }

    fun signInWithGoogle() {
        _isLoading.value = true
        _errorMessage.value = ""

        viewModelScope.launch {
            val result = googleAuthClient.signIn()

            if (result.data != null) {
                _userData.value = result.data
                _isUserLoggedIn.value = true
            } else {
                _errorMessage.value = result.errorMessage ?: "Sign-in failed"
            }

            _isLoading.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            googleAuthClient.signOut()
            _isUserLoggedIn.value = false
            _userData.value = null
        }
    }

    fun clearError() {
        _errorMessage.value = ""
    }
}