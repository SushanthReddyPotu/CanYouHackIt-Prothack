package com.example.communityai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.communityai.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel = viewModel<AuthViewModel>()
            val chatViewModel = viewModel<ChatViewModel>()
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    bottomBar = {
                        Surface(
                            shadowElevation = 2.dp
                        ) {
                            ChatBottomBar(
                                chatViewModel,
                                modifier = Modifier.navigationBarsPadding())
                        }
                    }
                ) { padding ->
                    NavHost(navController = navController, startDestination = "sign_in"){
                        composable("sign_in"){
                            GoogleSignInScreen(
                                viewModel = authViewModel,
                                onSignInSuccess = {
                                    navController.navigate("homeScreen")
                                }
                            )
                        }
                        composable("homeScreen"){
                            HomeScreen(
                                viewModel = authViewModel,
                                chatViewModel,
                                modifier = Modifier.padding(padding))
                        }
                    }
                }
            }
        }
    }
}
