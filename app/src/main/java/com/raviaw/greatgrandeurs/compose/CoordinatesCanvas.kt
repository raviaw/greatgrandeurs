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
import com.raviaw.greatgrandeurs.communication.ArduinoMode
import com.raviaw.greatgrandeurs.state.ArduinoState
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

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
    prepareArea()
    drawRaDecCoordinates(arduinoState.ra, arduinoState.dec, "Curr", true, textMeasurer, Color.Yellow)
    drawAzmAltCoordinates(arduinoState.azm, arduinoState.alt, "Curr", true, textMeasurer, Color.Cyan)

    if (arduinoState.activeMode == ArduinoMode.MODE_CALIBRATING || arduinoState.activeMode == ArduinoMode.MODE_FIND) {
      drawRaDecCoordinates(arduinoState.lastStarRa, arduinoState.lastStarDec, "Last Star", true, textMeasurer, Color.Red)
    }
    if (arduinoState.calibrated) {
      drawAzmAltCoordinates(arduinoState.currentMotorAzm, arduinoState.currentMotorAlt, "Motor", true, textMeasurer, Color.Cyan)
    }
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Standard page")
@Composable
fun CoordinateCanvasPreview() {
  GreatGrandeursTheme {
    CoordinatesCanvas(ArduinoState().apply {
      ra = 10.0
      dec = 20.0
      azm = 15.0
      alt = 30.0
      lastStarRa = 15.0
      lastStarDec = 25.0
      currentMotorAlt = 20.0
      currentMotorAzm = 28.0
      calibrated = true
      activeMode = ArduinoMode.MODE_FIND
    }
    )
  }
}
