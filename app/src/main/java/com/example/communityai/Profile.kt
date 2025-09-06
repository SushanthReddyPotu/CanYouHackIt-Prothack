package com.example.communityai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen(
    message: Message,
    modifier: Modifier = Modifier) {
    val user = Firebase.auth.currentUser ?: return
    Column {
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .size(40.dp)
        )
    }
}