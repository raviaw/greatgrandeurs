//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.CalibrationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CalibrateMoveDialog(modifier: Modifier = Modifier, calibrationState: CalibrationState, dialogVisible: Boolean, onDismissRequest: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

  var calibrateStarTarget by remember { mutableStateOf<StarTargets.Target?>(null) }

  LaunchedEffect(Unit) {
    while (true) {
      calibrateStarTarget = calibrationState.currentCalibrating

      //
      delay(500.milliseconds)
    }
  }

  if (dialogVisible) {
    Dialog(onDismissRequest = onDismissRequest) {
      Column(
        modifier = Modifier
          .statusBarsPadding()
          .standardPadding()
          .verticalScroll(rememberScrollState())
          .fillMaxSize()
          .safeDrawingPadding(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
      ) {
        Text(modifier = rowModifier, style = MaterialTheme.typography.headlineMedium, text = "Calibration")
        VerticalSpacer()
        Text(
          modifier = rowModifier,
          style = MaterialTheme.typography.headlineSmall,
          text = if (calibrateStarTarget == null) "Waiting" else "Find ${calibrateStarTarget!!.targetName}"
        )
        VerticalSpacer()
      }
    }
  }
}
