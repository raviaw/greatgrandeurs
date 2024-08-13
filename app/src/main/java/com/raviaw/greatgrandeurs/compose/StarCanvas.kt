//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.mapDouble
import com.raviaw.greatgrandeurs.tracking.StarTargets

@Composable
fun StarCanvas(targets: List<StarCanvasTarget>) {
  val textMeasurer = rememberTextMeasurer()
  Box(
    modifier = Modifier
      .fillMaxSize()
      .defaultMinSize(minHeight = 200.dp)
      .drawBehind { drawCoordinates(targets = targets, textMeasurer = textMeasurer) }
  )
}

private fun DrawScope.drawCoordinates(targets: List<StarCanvasTarget>, textMeasurer: TextMeasurer) {
  try {
    val canvasQuadrantSize = size
    drawRect(
      color = Color.Black,
      size = canvasQuadrantSize
    )
    for (starTarget in targets) {
      val raLine = mapDouble(starTarget.starTarget.raNum, 0.0, 24.0, 0.0, size.width * 1.0).toFloat()
      drawLine(Color.Yellow, Offset(raLine, 0.0f), Offset(raLine, size.height))
      drawText(
        textMeasurer = textMeasurer,
        text = "${starTarget.targetName} RA: " + starTarget.starTarget.raShort,
        style = TextStyle(
          fontSize = 6.sp,
          color = starTarget.targetColor,
          background = Color.Black
        ),
        topLeft = Offset(raLine, 0.0f).plus(Offset(5f, 0.0f))
      )

      val decLine = mapDouble(starTarget.starTarget.decNum, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
      drawLine(Color.Yellow, Offset(0.0f, decLine), Offset(size.width, decLine))
      drawText(
        textMeasurer = textMeasurer,
        text = "${starTarget.targetName} DEC: " + starTarget.starTarget.decShort,
        style = TextStyle(
          fontSize = 6.sp,
          color = starTarget.targetColor,
          background = Color.Black
        ),
        topLeft = Offset(0.0f, decLine).plus(Offset(5f, 5f))
      )
    }
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

class StarCanvasTarget(val targetName: String, val targetColor: Color, val starTarget: StarTargets.Target)
