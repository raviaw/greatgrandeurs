//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.CalibrationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CalibrationScreen(modifier: Modifier = Modifier, calibrationState: CalibrationState, starTargets: StarTargets, onBackClick: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

  var starCanvasTargets by remember { mutableStateOf<List<StarCanvasTarget>>(emptyList()) }

  LaunchedEffect(Unit) {
    while (true) {
      val localCanvasTargets = mutableListOf<StarCanvasTarget>()

      calibrationState.firstStarTarget?.let {
        localCanvasTargets.add(StarCanvasTarget(it.targetName, Color.Yellow, it))
      }
      calibrationState.secondStarTarget?.let {
        localCanvasTargets.add(StarCanvasTarget(it.targetName, Color.Cyan, it))
      }

      starCanvasTargets = localCanvasTargets

      //
      delay(500.milliseconds)
    }
  }

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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "First Star")
    StarPicker(
      rowModifier = rowModifier,
      textColumnModifier = textColumnModifier,
      starTargets = starTargets,
      color = Color.Yellow,
      thisTarget = { calibrationState.firstStarTarget },
      otherTarget = { calibrationState.secondStarTarget },
      otherTargetColor = Color.Cyan
    ) { calibrationState.firstStarTarget = it }
    VerticalSpacer()
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Second Star")
    StarPicker(
      rowModifier = rowModifier,
      textColumnModifier = textColumnModifier,
      starTargets = starTargets,
      color = Color.Cyan,
      thisTarget = { calibrationState.secondStarTarget },
      otherTarget = { calibrationState.firstStarTarget },
      otherTargetColor = Color.Yellow
    ) { calibrationState.secondStarTarget = it }
    VerticalSpacer()
    StarCanvas(modifier = Modifier.standardPadding(), targets = starCanvasTargets)
  }
}

@Composable
private fun StarPicker(
  rowModifier: Modifier,
  textColumnModifier: Modifier,
  starTargets: StarTargets,
  color: Color,
  thisTarget: () -> StarTargets.Target?,
  otherTarget: () -> StarTargets.Target?,
  otherTargetColor: Color,
  onStarSelected: (StarTargets.Target) -> Unit,
) {
  var starExpanded by remember { mutableStateOf(false) }

  //
  Row(verticalAlignment = Alignment.CenterVertically, modifier = rowModifier) {
    OutlinedTextField(
      value = thisTarget()?.targetName.orEmpty(),
      onValueChange = {},
      readOnly = true,
      label = { Text("Star name") })
    HorizontalSpacer()
    IconButton(modifier = textColumnModifier.weight(0.1f), onClick = { starExpanded = !starExpanded }) {
      Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = "More"
      )
    }
    HorizontalSpacer()
    DropdownMenu(
      expanded = starExpanded,
      onDismissRequest = { starExpanded = false },
      modifier = textColumnModifier.weight(0.6f)
    ) {
      starTargets.targets.map {
        val otherTargetResolved = otherTarget()
        val otherStar = it == otherTargetResolved
        DropdownMenuItem(modifier = Modifier.fillMaxWidth(),
          enabled = !otherStar,
          text = {
            Column {
              Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxSize(0.5f), text = it.targetName)
                Text(modifier = Modifier.width(90.dp), text = it.raShort)
                Text(modifier = Modifier.width(90.dp), text = it.decShort)
              }
              SimpleStarCanvas(
                target = it,
                width = 50.dp,
                height = 40.dp,
                color = if (otherStar) otherTargetColor else color
              )
            }
          },
          onClick = {
            starExpanded = false
            onStarSelected(it)
          }
        )
      }
    }
  }
}