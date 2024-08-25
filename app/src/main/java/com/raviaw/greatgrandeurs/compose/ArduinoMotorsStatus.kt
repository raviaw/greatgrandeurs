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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.StandardPadding
import com.raviaw.greatgrandeurs.state.ApplicationState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ArduinoMotorStatus(modifier: Modifier = Modifier, applicationState: ApplicationState) {
  val shortColumnModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = StandardPadding, vertical = 0.dp)
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  var horizontalMotorPosition by remember { mutableLongStateOf(0) }
  var verticalMotorPosition by remember { mutableLongStateOf(0) }
  var horizontalEncoderPosition by remember { mutableLongStateOf(0) }
  var verticalEncoderPosition by remember { mutableLongStateOf(0) }

  LaunchedEffect(Unit) {
    while (true) {
      horizontalMotorPosition = applicationState.arduinoState.horizontalMotorPosition
      verticalMotorPosition = applicationState.arduinoState.verticalMotorPosition
      horizontalEncoderPosition = applicationState.arduinoState.horizontalEncoderPosition
      verticalEncoderPosition = applicationState.arduinoState.verticalEncoderPosition

      delay(500.milliseconds)
    }
  }

  Row(verticalAlignment = Alignment.Top, modifier = shortColumnModifier) {
    Text(modifier = textModifier.weight(1.0f), text = "Motor H: $horizontalMotorPosition")
    HorizontalSpacer()
    Text(modifier = textModifier.weight(1.0f), text = "Motor V: $verticalMotorPosition")
  }
  Row(verticalAlignment = Alignment.Top, modifier = shortColumnModifier) {
    Text(modifier = textModifier.weight(1.0f), text = "Encoder H: $horizontalEncoderPosition")
    HorizontalSpacer()
    Text(modifier = textModifier.weight(1.0f), text = "Encoder V: $verticalEncoderPosition")
  }
}
