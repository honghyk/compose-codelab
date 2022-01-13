package com.example.compose_codelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose_codelab.ui.theme.ComposeCodelabTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ComposeCodelabTheme {
        MyApp()
      }
    }
  }
}

@Composable
private fun MyApp() {
  var shouldShowOnboarding by remember { mutableStateOf(true) }

  if (shouldShowOnboarding) {
    OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
  } else {
    Greetings()
  }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {
  var shouldShowOnboarding by remember { mutableStateOf(true) }

  Surface {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("Welcome to the Basic Codelab!")
      Button(
        modifier = Modifier.padding(vertical = 24.dp),
        onClick = onContinueClicked
      ) {
        Text("Continue")
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
  ComposeCodelabTheme {
    OnboardingScreen(onContinueClicked = { })
  }
}

@Composable
private fun Greetings(names: List<String> = listOf("World", "Compose")) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    for (name in names) {
      Greeting(name)
    }
  }
}

@Composable
fun Greeting(name: String) {
  val expanded = remember { mutableStateOf(false) }
  val extraPadding = if (expanded.value) 48.dp else 0.dp

  Surface(
    color = MaterialTheme.colors.primary,
    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
  ) {
    Row(modifier = Modifier.padding(24.dp)) {
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(bottom = extraPadding)
      ) {
        Text(text = "Hello, ")
        Text(text = name)
      }
      OutlinedButton(
        onClick = { expanded.value = !expanded.value }
      ) {
        Text(if (expanded.value) "Show less" else "Show more")
      }
    }
  }
}

@Preview(showBackground = true, name = "Text Preview")
@Composable
fun DefaultPreview() {
  ComposeCodelabTheme {
    Greetings()
  }
}