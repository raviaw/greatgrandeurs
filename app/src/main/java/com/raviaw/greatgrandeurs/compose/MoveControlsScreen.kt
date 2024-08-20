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
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.ArduinoCommander
import com.raviaw.greatgrandeurs.mapFloat
import com.raviaw.greatgrandeurs.mapInt
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.CalibrationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

private val midway = 8
private val numberOfLines = midway * 2 + 2
private val boxSize = (midway * 2 + 1).toFloat()
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
  calibrationState: CalibrationState,
  arduinoCommander: ArduinoCommander,
  onDismiss: () -> Unit
) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

  var calibrateStarTarget by remember { mutableStateOf<StarTargets.Target?>(null) }

  var layoutSize by remember { mutableStateOf<Size?>(null) }
  var activePoint by remember { mutableStateOf(Point(midway, midway)) }
  var boxInput by remember { mutableStateOf<BoxInput?>(null) }
  var speed by remember { mutableFloatStateOf(50f) }

  var horizontalCommand by remember { mutableStateOf("Stopped") }
  var verticalCommand by remember { mutableStateOf("Stopped") }

  var maxSpeed by remember { mutableIntStateOf(0) }

  var activeColor by remember { mutableStateOf<Color>(Color.Black) }

  LaunchedEffect(Unit) {
    while (true) {
      calibrateStarTarget = calibrationState.currentCalibrating

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

    //
    // Sends the command
    // All parameters are between -100..100
    val sendHorizontalSpeed = mapInt(horizontalSpeed, -midway, midway, -100, 100)
    val sendVerticalSpeed = mapInt(verticalSpeed, -midway, midway, -100, 100)
    arduinoCommander.sendCalibratingMoveSpeed(sendHorizontalSpeed, sendVerticalSpeed, speed)
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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineMedium, text = "Calibration")
    Text(
      modifier = rowModifier,
      style = MaterialTheme.typography.headlineSmall,
      text = if (calibrateStarTarget == null) "Waiting" else "Find ${calibrateStarTarget!!.targetName}"
    )
    if (calibrateStarTarget != null) {
      Text(modifier = rowModifier, text = "Star position:")
      SimpleStarCanvas(target = calibrateStarTarget!!, height = 40.dp, color = Color.White)
    }
    VerticalSpacer()
    Text(modifier = rowModifier, text = "Speed/ move:")
    MoveGridCanvas(
      boxSize = boxSize,
      numberOfLines = numberOfLines,
      layoutSize = layoutSize,
      activePoint = activePoint,
      activeColor = activeColor,
      speed = speed,
      onBoxInput = { boxInput = it },
      onSpeedChange = { speed = it },
      onGlobalPositioned = { layoutSize = it.size.toSize() })
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
          activePoint = activePoint.copy(x = midway)
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
          activePoint = activePoint.copy(y = midway)
        }
      )
    }
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

@Suppress("SameParameterValue")
private fun withinBoundaries(value: Int, min: Int, max: Int): Int {
  return when {
    value < min -> min
    value > max -> max
    else -> value
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Move controls dialog")
@Composable
fun MoveControlsDialogPreview() {
  GreatGrandeursTheme {
    MoveControlsScreen(
      modifier = Modifier,
      calibrationState = CalibrationState().apply {
        this.currentCalibrating = StarTargets.Target(
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
