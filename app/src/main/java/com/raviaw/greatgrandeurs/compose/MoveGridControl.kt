//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.tooling.preview.Preview
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.ArduinoCommander
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

val midway = 10
val numberOfLines = midway * 2 + 2
val boxSize = (midway * 2 + 1).toFloat()

@Composable
fun MoveGridControl(
  modifier: Modifier = Modifier,
  layoutSize: Size?,
  horizontalCommand: String,
  verticalCommand: String,
  applicationState: ApplicationState,
  activePoint: Point,
  activeColor: Color,
  speed: Float,
  showFullArduinoStatus: Boolean = false,
  onActivePointChange: (Point) -> Unit,
  onSpeedChange: (Float) -> Unit,
  onBoxInput: (BoxInput) -> Unit,
  onGlobalPositioned: (LayoutCoordinates) -> Unit
) {
  val textColumnModifier = Modifier
    .fillMaxWidth()
    .standardPadding()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  if (showFullArduinoStatus) {
    FullArduinoStatus(applicationState = applicationState)
  } else {
    ArduinoMotorStatus(applicationState = applicationState)
  }
  MoveGridCanvas(
    boxSize = boxSize,
    numberOfLines = numberOfLines,
    layoutSize = layoutSize,
    activePoint = activePoint,
    activeColor = activeColor,
    speed = speed,
    onBoxInput = onBoxInput,
    onSpeedChange = onSpeedChange,
    onGlobalPositioned = onGlobalPositioned
  )
  VerticalSpacer()
  Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
    Button(
      content = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("H: $horizontalCommand")
          Text("Click to stop")
        }
      },
      modifier = textModifier
        .weight(1.0f),
      onClick = {
        onActivePointChange(activePoint.copy(x = midway))
      }
    )
    HorizontalSpacer()
    Button(
      content = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("V: $verticalCommand")
          Text("Click to stop")
        }
      },
      modifier = textModifier
        .weight(1.0f),
      onClick = {
        onActivePointChange(activePoint.copy(y = midway))
      }
    )
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Move controls dialog")
@Composable
fun MoveGridControlPreview() {
  GreatGrandeursTheme {
    MoveControlsScreen(
      modifier = Modifier,
      applicationState = ApplicationState().apply {
        calibrationState.currentCalibrating = StarTargets.Target(
          starIndex = 0,
          target = "Betelgeuse",
          targetName = "Betelgeuse",
          special = "",
          ra = "5",
          dec = "7"
        )
      },
      arduinoCommander = object : ArduinoCommander {
        override val connected: Boolean = false
        override val arduinoSlaveMode: Boolean = false
        override val arduinoLightsOn: Boolean = true
      },
      onDismiss = {}
    )
  }
}
