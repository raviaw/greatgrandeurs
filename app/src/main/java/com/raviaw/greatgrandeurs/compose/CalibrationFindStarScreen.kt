//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.ArduinoCommander
import com.raviaw.greatgrandeurs.mapFloat
import com.raviaw.greatgrandeurs.mapInt
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import com.raviaw.greatgrandeurs.withinBoundaries
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

private const val maxRed = 120
private const val maxGreen = 50
private const val maxBlue = 50
private val colorMap = (1..midway).associateWith { index ->
  Color(
    red = mapInt(input = index, inMin = 1, inMax = midway, outMin = 10, outMax = maxRed),
    green = mapInt(input = index, inMin = 1, inMax = midway, outMin = 10, outMax = maxGreen),
    blue = mapInt(input = index, inMin = 1, inMax = midway, outMin = 10, outMax = maxBlue)
  )
}

@Composable
fun MoveControlsScreen(
  modifier: Modifier = Modifier,
  applicationState: ApplicationState,
  arduinoCommander: ArduinoCommander,
  onDismiss: () -> Unit
) {
  val textColumnModifier = Modifier
    .fillMaxWidth()
    .standardPadding()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

  var calibrateStarTarget by remember { mutableStateOf<StarTargets.Target?>(null) }

  var layoutSize by remember { mutableStateOf<Size?>(null) }
  var activePoint by remember { mutableStateOf(Point(midway, midway)) }
  var boxInput by remember { mutableStateOf<BoxInput?>(null) }
  var speed by remember { mutableFloatStateOf(5f) }

  var horizontalCommand by remember { mutableStateOf("Stopped") }
  var verticalCommand by remember { mutableStateOf("Stopped") }

  var maxSpeed by remember { mutableIntStateOf(0) }

  var activeColor by remember { mutableStateOf<Color>(Color.Black) }

  var lastSentHorizontalSpeed by remember { mutableIntStateOf(0) }
  var lastSentVerticalSpeed by remember { mutableIntStateOf(0) }
  var lastHorizontalSpeed by remember { mutableIntStateOf(0) }
  var lastVerticalSpeed by remember { mutableIntStateOf(0) }
  var lastSentSpeed by remember { mutableFloatStateOf(5F) }
  var lastSpeed by remember { mutableFloatStateOf(5F) }

  LaunchedEffect(Unit) {
    while (true) {
      calibrateStarTarget = applicationState.calibrationState.currentCalibrating

      //
      delay(500.milliseconds)
    }
  }

  LaunchedEffect(boxInput, layoutSize) {
    boxInput?.let { boxInput ->
      layoutSize?.let { layoutSize ->
        activePoint =
          Point(
            withinBoundaries(
              value = mapFloat(
                input = boxInput.x,
                inMin = 0f,
                inMax = layoutSize.width,
                outMin = 0f,
                outMax = boxSize
              ).toInt(),
              min = 0,
              max = midway * 2
            ),
            withinBoundaries(
              value = mapFloat(
                input = boxInput.y,
                inMin = 0f,
                inMax = layoutSize.height,
                outMin = 0f,
                outMax = boxSize
              ).toInt(),
              min = 0,
              max = midway * 2
            )
          )
      }
    }
  }

  LaunchedEffect(activePoint, speed) {
    val horizontalSpeed = activePoint.x - midway
    val verticalSpeed = activePoint.y - midway

    //
    // Formats the information
    val format = DecimalFormat("0")
    format.positivePrefix = "+"
    if (horizontalSpeed == 0) {
      horizontalCommand = "Stopped"
    } else {
      horizontalCommand = "Moving at ${format.format(horizontalSpeed)}"
    }
    if (verticalSpeed == 0) {
      verticalCommand = "Stopped"
    } else {
      verticalCommand = "Moving at ${format.format(verticalSpeed)}"
    }
    maxSpeed = max(abs(horizontalSpeed), abs(verticalSpeed))
    activeColor = colorMap.getOrElse(maxSpeed) { Color.Black }

    lastHorizontalSpeed = horizontalSpeed
    lastVerticalSpeed = verticalSpeed
    lastSpeed = speed
  }

  LaunchedEffect(Unit) {
    while (true) {
      //
      // Sends the command twice per second if things are changing
      // All parameters are between -100..100
      if (lastSentSpeed != lastSpeed || lastSentHorizontalSpeed != lastHorizontalSpeed || lastSentVerticalSpeed != lastVerticalSpeed) {
        arduinoCommander.sendCalibratingMoveSpeed(lastHorizontalSpeed, lastVerticalSpeed, lastSpeed)
        lastSentSpeed = lastSpeed
        lastSentVerticalSpeed = lastVerticalSpeed
        lastSentHorizontalSpeed = lastHorizontalSpeed
      }
      delay(500.milliseconds)
    }
  }

  val onDismissRequest: () -> Unit = {
    onDismiss()
  }

  val onDone: () -> Unit = {
    arduinoCommander.sendStoreCalibration()
    arduinoCommander.sendCalibratingMoveStop()
    onDismissRequest()
  }

  Column(
    modifier = modifier
      .statusBarsPadding()
      .standardPadding()
      .verticalScroll(rememberScrollState())
      .fillMaxSize()
      .safeDrawingPadding(),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top
  ) {
    Text(
      modifier = rowModifier,
      style = MaterialTheme.typography.headlineMedium,
      text = "Calibration/ ${if (calibrateStarTarget == null) "Waiting" else "Find ${calibrateStarTarget!!.targetName}"}"
    )
    if (calibrateStarTarget != null) {
      Text(modifier = rowModifier, text = "Star position:")
      SimpleStarCanvas(target = calibrateStarTarget!!, height = 40.dp, color = Color.White)
    }
    VerticalSpacer()
    MoveGridControl(
      applicationState = applicationState,
      layoutSize = layoutSize,
      horizontalCommand = horizontalCommand,
      verticalCommand = verticalCommand,
      activePoint = activePoint,
      activeColor = activeColor,
      speed = speed,
      onActivePointChange = { activePoint = it },
      onSpeedChange = { speed = it },
      onBoxInput = { boxInput = it },
      onGlobalPositioned = { layoutSize = it.size.toSize() }
    )
    VerticalSpacer()
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Done") },
        modifier = textModifier.weight(1.0f),
        onClick = { onDone() }
      )
    }
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Move controls dialog")
@Composable
fun MoveControlsDialogPreview() {
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
        override val laserOn: Boolean = false
      },
      onDismiss = {}
    )
  }
}
