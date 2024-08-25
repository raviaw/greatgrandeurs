//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.StandardPadding
import com.raviaw.greatgrandeurs.communication.ArduinoMode
import com.raviaw.greatgrandeurs.formatCoordinate
import com.raviaw.greatgrandeurs.state.ApplicationState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FullArduinoStatus(modifier: Modifier = Modifier, applicationState: ApplicationState) {
  val textColumnModifier = Modifier
    .fillMaxWidth()
    .padding(StandardPadding)
  val shortColumnModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = StandardPadding, vertical = 0.dp)
  val textModifier = Modifier
    .fillMaxWidth(0.4f)
  val basePadding = Modifier.padding(StandardPadding)

  var rightAscension by remember { mutableDoubleStateOf(0.0) }
  var declination by remember { mutableDoubleStateOf(0.0) }
  var altitude by remember { mutableDoubleStateOf(0.0) }
  var azimuth by remember { mutableDoubleStateOf(0.0) }
  var horizontalMotorPosition by remember { mutableLongStateOf(0) }
  var verticalMotorPosition by remember { mutableLongStateOf(0) }
  var horizontalEncoderPosition by remember { mutableLongStateOf(0) }
  var verticalEncoderPosition by remember { mutableLongStateOf(0) }
  var activeMode by remember { mutableStateOf(ArduinoMode.MODE_MENU) }
  var calibrated by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    while (true) {
      rightAscension = applicationState.arduinoState.ra
      declination = applicationState.arduinoState.dec
      altitude = applicationState.arduinoState.alt
      azimuth = applicationState.arduinoState.azm
      horizontalMotorPosition = applicationState.arduinoState.horizontalMotorPosition
      verticalMotorPosition = applicationState.arduinoState.verticalMotorPosition
      horizontalEncoderPosition = applicationState.arduinoState.horizontalEncoderPosition
      verticalEncoderPosition = applicationState.arduinoState.verticalEncoderPosition
      activeMode = applicationState.arduinoState.activeMode
      calibrated = applicationState.arduinoState.calibrated

      delay(500.milliseconds)
    }
  }

  Row(verticalAlignment = Alignment.Top, modifier = shortColumnModifier) {
    Text(
      modifier = textModifier.weight(1.0f),
      text = "RA: ${rightAscension.formatCoordinate()}"
    )
    HorizontalSpacer()
    Text(
      modifier = textModifier.weight(1.0f),
      text = "DEC: ${declination.formatCoordinate()}"
    )
  }
  Row(verticalAlignment = Alignment.Top, modifier = shortColumnModifier) {
    Text(
      modifier = textModifier.weight(1.0f),
      text = "ALT: ${altitude.formatCoordinate()}"
    )
    Text(
      modifier = textModifier.weight(1.0f),
      text = "AZM: ${azimuth.formatCoordinate()}"
    )
  }
  ArduinoMotorStatus(applicationState = applicationState)
  Row(verticalAlignment = Alignment.Top, modifier = shortColumnModifier) {
    Text(
      modifier = textModifier.weight(1.0f),
      text = "Mode: ${activeMode.name.substringAfter("MODE_").replace("_", " ").lowercase().replaceFirstChar { it.uppercaseChar() }}"
    )
    HorizontalSpacer()
    Text(modifier = textModifier.weight(1.0f), text = "Calibrated: ${if (calibrated) "Yes" else "No"}")
  }
}
