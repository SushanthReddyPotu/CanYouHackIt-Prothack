package com.example.communityai

import android.service.autofill.Validators.and
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.integrity.internal.s
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch

import kotlin.jvm.java

class ChatViewModel : ViewModel() {
    private val database = Firebase.database
    private val messagesRef = database.getReference("messages")

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    init {
        loadMessages()
    }

    val api = retrofit.create(ApiService::class.java)

    suspend fun requestSummary(text: String): String? {
        val response = api.summarize(SummaryRequest(text = text))
        return if (response.isSuccessful) {
            response.body()?.summary
        } else {
            null
        }
    }

    suspend fun analyzeSentiment(text: String): SentimentResponse {
        val response = api.analyzeSentiment(SentimentRequest(text))
        return response.body() ?: SentimentResponse("NEUTRAL", 0.5f)
    }


    private fun loadMessages() {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newMessages = snapshot.children.mapNotNull {
                    it.getValue(Message::class.java)
                }
                _messages.clear()
                _messages.addAll(newMessages.sortedBy { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database error: ${error.message}")
            }
        })
    }

    fun sendMessage(text: String) {
        val user = Firebase.auth.currentUser ?: return

        val message = Message(
            text = text,
            senderId = user.uid,
            senderName = user.displayName ?: "Anonymous",
            senderAvatarUrl = user.photoUrl.toString()
        )

        messagesRef.push().setValue(message)
    }
}
data class Message(
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderAvatarUrl: String = "",
)