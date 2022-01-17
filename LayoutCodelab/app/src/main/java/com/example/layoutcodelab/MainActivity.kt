package com.example.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.layoutcodelab.ui.theme.LayoutCodelabTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LayoutCodelabTheme {
        LayoutsCodelab()
      }
    }
  }
}

@Composable
fun LayoutsCodelab() {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "LayoutsCodelab")
        },
        actions = {
          IconButton(onClick = { }) {
            Icon(
              Icons.Filled.Favorite,
              contentDescription = null
            )
          }
        }
      )
    }
  ) { innerPadding ->
    BodyContent(Modifier.padding(innerPadding).padding(8.dp))
  }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Text(text = "Hi there!")
    Text(text = "Thanks for going through the Layouts codelab")
  }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
  LayoutCodelabTheme {
    LayoutsCodelab()
  }
}