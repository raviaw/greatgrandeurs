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
import com.raviaw.greatgrandeurs.communication.ArduinoState
import com.raviaw.greatgrandeurs.mapDouble

@Composable
fun CoordinatesCanvas(arduinoState: ArduinoState) {
  val textMeasurer = rememberTextMeasurer()
  Box(
    modifier = Modifier
      .fillMaxSize()
      .defaultMinSize(minHeight = 200.dp)
      .drawBehind { drawCoordinates(arduinoState, textMeasurer) }
  )
}

private fun DrawScope.drawCoordinates(arduinoState: ArduinoState, textMeasurer: TextMeasurer) {
  try {
    val canvasQuadrantSize = size
    drawRect(
      color = Color.Black,
      size = canvasQuadrantSize
    )
    val raLine = mapDouble(arduinoState.ra, 0.0, 24.0, 0.0, size.width * 1.0).toFloat()
    drawLine(Color.Yellow, Offset(raLine, 0.0f), Offset(raLine, size.height))
    drawText(
      textMeasurer = textMeasurer,
      text = "RA: " + arduinoState.ra,
      style = TextStyle(
        fontSize = 6.sp,
        color = Color.Red,
        background = Color.Black
      ),
      topLeft = Offset(raLine, 0.0f).plus(Offset(5f, 0.0f))
    )

    val decLine = mapDouble(arduinoState.dec, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
    drawLine(Color.Yellow, Offset(0.0f, decLine), Offset(size.width, decLine))
    drawText(
      textMeasurer = textMeasurer,
      text = "DEC: " + arduinoState.ra,
      style = TextStyle(
        fontSize = 6.sp,
        color = Color.Red,
        background = Color.Black
      ),
      topLeft = Offset(0.0f, decLine).plus(Offset(5f, 5f))
    )

    val azmLine = mapDouble(arduinoState.azm, 0.0, 360.0, 0.0, size.width * 1.0).toFloat()
    drawLine(Color.Cyan, Offset(azmLine, 0.0f), Offset(azmLine, size.height))
    drawText(
      textMeasurer = textMeasurer,
      text = "AZM: " + arduinoState.azm,
      style = TextStyle(
        fontSize = 6.sp,
        color = Color.Red,
        background = Color.Black
      ),
      topLeft = Offset(azmLine, 0.0f).plus(Offset(5f, 0.0f))
    )

    val altLine = mapDouble(arduinoState.alt, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
    drawLine(Color.Cyan, Offset(0.0f, altLine), Offset(size.width, altLine))
    drawText(
      textMeasurer = textMeasurer,
      text = "ALT: " + arduinoState.alt,
      style = TextStyle(
        fontSize = 6.sp,
        color = Color.Red,
        background = Color.Black
      ),
      topLeft = Offset(0.0f, altLine).plus(Offset(5f, 5f))
    )
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

