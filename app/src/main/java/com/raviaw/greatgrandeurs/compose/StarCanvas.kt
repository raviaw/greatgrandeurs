//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

@Composable
fun StarCanvas(modifier: Modifier, targets: List<StarCanvasTarget>) {
  val textMeasurer = rememberTextMeasurer()
  Box(
    modifier = modifier
      .fillMaxSize()
      .defaultMinSize(minHeight = 200.dp)
      .drawBehind { drawCoordinates(targets = targets, textMeasurer = textMeasurer) }
  )
}

private fun DrawScope.drawCoordinates(targets: List<StarCanvasTarget>, textMeasurer: TextMeasurer) {
  try {
    prepareArea()
    for (starTarget in targets) {
      drawRaDecCoordinates(
        ra = starTarget.starTarget.raNum,
        dec = starTarget.starTarget.decNum,
        targetName = starTarget.targetName,
        includeCaption = true,
        textMeasurer = textMeasurer,
        color = starTarget.targetColor
      )
    }
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

class StarCanvasTarget(val targetName: String, val targetColor: Color, val starTarget: StarTargets.Target)

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Standard page")
@Composable
fun StarCanvasPreview() {
  GreatGrandeursTheme {
    StarCanvas(
      modifier = Modifier,
      targets = listOf(
        StarCanvasTarget(targetName = "Test 1", targetColor = Color.Cyan, starTarget = StarTargets.Target(1, "Test 1", "Test 1", "", "15", "15")),
        StarCanvasTarget(targetName = "Test 2", targetColor = Color.Red, starTarget = StarTargets.Target(2, "Test 2", "Test 2", "", "20", "12"))
      )
    )
  }
}
