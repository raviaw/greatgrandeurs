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
import com.raviaw.greatgrandeurs.communication.ArduinoMode
import com.raviaw.greatgrandeurs.mapDouble
import com.raviaw.greatgrandeurs.state.ArduinoState

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

    if (arduinoState.activeMode == ArduinoMode.MODE_CALIBRATING || arduinoState.activeMode == ArduinoMode.MODE_FIND) {
      val lastStarRaLine = mapDouble(arduinoState.lastStarRa, 0.0, 24.0, 0.0, size.width * 1.0).toFloat()
      drawLine(color = Color.Red, start = Offset(lastStarRaLine, 0.0f), end = Offset(lastStarRaLine, size.height), alpha = 0.5f)
      drawText(
        textMeasurer = textMeasurer,
        text = "Last Star RA: " + arduinoState.ra,
        style = TextStyle(
          fontSize = 6.sp,
          color = Color.Red,
          background = Color.Black
        ),
        topLeft = Offset(lastStarRaLine, 0.0f).plus(Offset(5f, 0.0f))
      )

      val lastStarDecLine = mapDouble(arduinoState.lastStarDec, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
      drawLine(color = Color.Red, start = Offset(0.0f, lastStarDecLine), end = Offset(size.width, lastStarDecLine), alpha = 0.5f)
      drawText(
        textMeasurer = textMeasurer,
        text = "Last Star DEC: " + arduinoState.ra,
        style = TextStyle(
          fontSize = 6.sp,
          color = Color.Red,
          background = Color.Black
        ),
        topLeft = Offset(0.0f, lastStarDecLine).plus(Offset(5f, 5f))
      )
    }

    if (arduinoState.calibrated) {
      val currentMotorAzmLine = mapDouble(arduinoState.currentMotorAzm, 0.0, 360.0, 0.0, size.width * 1.0).toFloat()
      drawLine(color = Color.Green, start = Offset(currentMotorAzmLine, 0.0f), end = Offset(currentMotorAzmLine, size.height), alpha = 0.5f)
      drawText(
        textMeasurer = textMeasurer,
        text = "Motor AZM: " + arduinoState.azm,
        style = TextStyle(
          fontSize = 6.sp,
          color = Color.Green,
          background = Color.Black
        ),
        topLeft = Offset(currentMotorAzmLine, 0.0f).plus(Offset(5f, 0.0f))
      )

      val currentMotorAltLine = mapDouble(arduinoState.currentMotorAlt, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
      drawLine(color = Color.Green, start = Offset(0.0f, currentMotorAltLine), end = Offset(size.width, currentMotorAltLine), alpha = 0.5f)
      drawText(
        textMeasurer = textMeasurer,
        text = "Motor ALT: " + arduinoState.alt,
        style = TextStyle(
          fontSize = 6.sp,
          color = Color.Green,
          background = Color.Black
        ),
        topLeft = Offset(0.0f, currentMotorAltLine).plus(Offset(5f, 5f))
      )
    }
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

