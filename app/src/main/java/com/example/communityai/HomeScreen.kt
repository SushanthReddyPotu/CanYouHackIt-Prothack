package com.example.communityai

import android.R.attr.padding
import android.transition.ChangeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.communityai.ui.theme.AppTheme
import java.lang.reflect.GenericSignatureFormatError

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val userData = viewModel.userData.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f)) {
            items(chatViewModel.messages) { message ->
                MessageItem(viewModel = viewModel, message = message)
            }
        }
    }
}

@Composable
fun ChatBottomBar(
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {

    var textFieldValue by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceDim,
            modifier = Modifier.width(280.dp)
        ) {
            OutlinedTextField(
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                placeholder = { Text("Message ") },
            )
        }
        IconButton(
            onClick = {
                if (textFieldValue.isNotBlank()) {
                    chatViewModel.sendMessage(textFieldValue)
                    textFieldValue = ""
                }

            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
            ),
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.send_svgrepo_com),
                contentDescription = "Send",
                tint = Color.Black.copy(alpha = .5f),
                modifier = Modifier.size(25.dp)
            )
        }

    }
}

@Composable
fun ChatTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
    ){
        Text(
            text = "Chatting Room",
            modifier = Modifier.align(Alignment.Center).padding(12.dp),
            fontWeight = FontWeight.Bold
        )
    }
}
