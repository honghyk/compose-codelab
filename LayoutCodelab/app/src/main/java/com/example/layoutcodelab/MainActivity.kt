package com.example.layoutcodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Color
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
import kotlin.math.max

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

val topics = listOf(
  "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
  "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
  "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
  Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
    StaggeredGrid(modifier = modifier, rows = 5) {
      for (topic in topics) {
        Chip(modifier = Modifier.padding(8.dp), text = topic)
      }
    }
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

@Composable
fun StaggeredGrid(
  modifier: Modifier = Modifier,
  rows: Int = 3,
  content: @Composable () -> Unit
) {
  Layout(
    modifier = modifier,
    content = content
  ) { measureables, constraints ->

    val rowWidths = IntArray(rows) { 0 }
    val rowHeights = IntArray(rows) { 0 }

    val placeables = measureables.mapIndexed { index, measurable ->
      val placeable = measurable.measure(constraints)

      val row = index % rows
      rowWidths[row] += placeable.width
      rowHeights[row] = max(rowHeights[row], placeable.height)

      placeable
    }

    val width = rowWidths.maxOrNull()
      ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
    val height = rowHeights.sumOf { it }
      .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

    val rowY = IntArray(rows) { 0 }
    for (i in 1 until rows) {
      rowY[i] = rowY[i - 1] + rowHeights[i - 1]
    }

    layout(width, height) {
      val rowX = IntArray(rows) { 0 }

      placeables.forEachIndexed { index, placeable ->
        val row = index % rows
        placeable.placeRelative(
          x = rowX[row],
          y = rowY[row]
        )
        rowX[row] += placeable.width
      }
    }
  }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
  Card(
    modifier = modifier,
    border = BorderStroke(color = Color.Black, width = Dp.Hairline),
    shape = RoundedCornerShape(8.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier.size(16.dp, 16.dp)
          .background(color = MaterialTheme.colors.secondary)
      )
      Spacer(Modifier.width(4.dp))
      Text(text = text)
    }
  }
}

@Preview
@Composable
fun ChipPreview() {
  LayoutCodelabTheme {
    Chip(text = "Hi there")
  }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
  LayoutCodelabTheme {
    LayoutsCodelab()
  }
}