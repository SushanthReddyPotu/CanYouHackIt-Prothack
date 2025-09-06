package com.example.communityai

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItem(
    viewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val sheetState = rememberModalBottomSheetState()
        var showSummaryBottomSheet by remember { mutableStateOf(false) }
        var openDialog by remember { mutableStateOf(false) }
        var showSentimentBottomSheet by remember { mutableStateOf(false) }
        val userData by viewModel.userData.collectAsState()
        Row {
            AsyncImage(
                model = message.senderAvatarUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Row {
                    Text(
                        text = message.senderName,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer( modifier = Modifier.padding(4.dp))
                    Text(
                        text = SimpleDateFormat(
                            "HH:mm",
                            Locale.getDefault()
                        ).format(Date(message.timestamp)),
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = message.text,
                    modifier = Modifier.pointerInput(
                        Unit
                    ) {
                        detectTapGestures(
                            onLongPress = {
                                openDialog = true
                            }
                        )
                    }
                )
                if(openDialog){
                    DropdownMenu(
                        expanded = openDialog,
                        onDismissRequest = { openDialog = false }
                    ){
                        DropdownMenuItem(
                            trailingIcon = {
                                Icon(painter = painterResource(R.drawable.summary_svgrepo_com),
                                    contentDescription = "summarize",
                                    modifier = Modifier.size(24.dp)
                                    )
                            },
                            text = { Text("summaries") },
                            onClick = {
                                openDialog = false
                                showSummaryBottomSheet = true
                            }
                        )
                        DropdownMenuItem(
                            trailingIcon = {
                                Icon(painter = painterResource(R.drawable.sentiment_neutral_svgrepo_com),
                                    contentDescription = "sentiment",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            text = { Text("sentiment") },
                            onClick = {
                                openDialog = false
                                showSentimentBottomSheet = true
                            }
                        )
                    }
                }
                if (showSummaryBottomSheet) {
                    var response by remember { mutableStateOf<String?>(null) }

                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { showSummaryBottomSheet = false }
                    ) {
                        Text(
                            text = "Text Summary",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp).align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        LaunchedEffect(message.text) {
                            response = chatViewModel.requestSummary(message.text)
                        }
                        LazyColumn {
                            item {
                                Box(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    Text(response ?: "Loading...")
                                }
                            }
                        }
                    }
                }
                if (showSentimentBottomSheet) {
                    var response by remember { mutableStateOf<SentimentResponse?>(null) }
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { showSentimentBottomSheet = false }
                    ) {
                        Text(
                            text = "Sentiment Analysis",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp).align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        LaunchedEffect(message.text) {
                            response = chatViewModel.analyzeSentiment(message.text)
                            Log.d("SentimentAnalysis", "Response: $response")
                        }
                        LazyColumn {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                                ) {
                                    Text(
                                        text = " ${response?.label}",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}