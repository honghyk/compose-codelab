package com.example.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.layoutcodelab.ui.theme.LayoutCodelabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LayoutCodelabTheme {
        LazyList()
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
  MyOwnColumn(modifier = modifier.padding(8.dp)) {
    Text("MyOwnColumn")
    Text(text = "Hi there!")
    Text(text = "Thanks for going through the Layouts codelab")
  }
}

@Composable
fun LazyList() {
  val listSize = 100
  val scrollState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  Column {
    Row {
      Button(
        modifier = Modifier.weight(1f),
        onClick = {
          coroutineScope.launch { scrollState.animateScrollToItem(0) }
        }) {
        Text("Scroll to the top")
      }
      Button(
        modifier = Modifier.weight(1f),
        onClick = {
          coroutineScope.launch { scrollState.animateScrollToItem(listSize - 1) }
        }) {
        Text("Scroll to the end")
      }
    }

    LazyColumn(state = scrollState) {
      items(100) {
        ImageListItem(it)
      }
    }
  }
}

@Composable
fun ImageListItem(index: Int) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Image(
      painter = rememberImagePainter(
        data = "https://developer.android.com/images/brand/Android_Robot.png"
      ),
      contentDescription = "Android Logo",
      modifier = Modifier.size(50.dp)
    )
    Spacer(Modifier.width(10.dp))
    Text("Item $index", style = MaterialTheme.typography.subtitle1)
  }
}

fun Modifier.firstBaselineToTop(
  firstBaselineToTop: Dp
) = this.then(
  layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
      placeable.placeRelative(0, placeableY)
    }
  }
)

@Composable
fun MyOwnColumn(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Layout(
    modifier = modifier,
    content = content,
  ) { measurables, constraints ->
    val placeables = measurables.map { measurable ->
      measurable.measure(constraints)
    }
    var yPosition = 0

    layout(constraints.maxWidth, constraints.maxHeight) {
      placeables.forEach { placeable ->
        placeable.placeRelative(x = 0, y = yPosition)

        yPosition += placeable.height
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
  LayoutCodelabTheme {
    Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
  }
}

@Preview(showBackground = true)
@Composable
fun TextWithNormalPaddingPreview() {
  LayoutCodelabTheme {
    Text("Hi there!", Modifier.padding(top = 32.dp))
  }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
  LayoutCodelabTheme {
    LayoutsCodelab()
  }
}

@Preview(
  showBackground = true,
  widthDp = 320,
  heightDp = 480
)
@Composable
fun LazyListPreview() {
  LayoutCodelabTheme {
    LazyList()
  }
}