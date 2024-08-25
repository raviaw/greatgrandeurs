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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.StandardPadding
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.ArduinoCommander
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FindScreen(
  modifier: Modifier = Modifier,
  applicationState: ApplicationState,
  starTargets: StarTargets,
  arduinoCommander: ArduinoCommander,
  onBackClick: () -> Unit
) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()
  val basePadding = Modifier.padding(StandardPadding)

  var starCanvasTargets by remember { mutableStateOf<List<StarCanvasTarget>>(emptyList()) }
  var findStar by remember { mutableStateOf<StarTargets.Target?>(null) }

  var findRaHours by rememberSaveable { mutableIntStateOf(0) }
  var findRaMinutes by rememberSaveable { mutableIntStateOf(0) }
  var findRaSeconds by rememberSaveable { mutableFloatStateOf(0F) }
  var findDecHours by rememberSaveable { mutableIntStateOf(0) }
  var findDecMinutes by rememberSaveable { mutableIntStateOf(0) }
  var findDecSeconds by rememberSaveable { mutableFloatStateOf(0F) }

  LaunchedEffect(Unit) {
    while (true) {
      val localCanvasTargets = mutableListOf<StarCanvasTarget>()

      applicationState.findStar?.let {
        localCanvasTargets.add(StarCanvasTarget(it.targetName, Color.Yellow, it))
      }
      findStar = applicationState.findStar

      starCanvasTargets = localCanvasTargets

      //
      delay(500.milliseconds)
    }
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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineMedium, text = "Find")
    VerticalSpacer()
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Star/ Celestial Body")
    StarPicker(
      rowModifier = rowModifier,
      textColumnModifier = textColumnModifier,
      starTargets = starTargets,
      thisTarget = { findStar }
    ) { applicationState.findStar = it }
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Find Star") },
        //enabled = bluetoothConnected,
        enabled = findStar != null,
        modifier = textModifier.weight(1.0f),
        onClick = { onStarFound(applicationState, arduinoCommander, onBackClick) }
      )
    }
    VerticalSpacer()
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findRaHours.toString(),
        onValueChange = { findRaHours = it.toInt() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("RA Hours") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findRaMinutes.toString(),
        onValueChange = { findRaMinutes = it.toInt() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("RA Minutes") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findRaSeconds.toString(),
        onValueChange = { findRaSeconds = it.toFloat() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("RA Seconds") })
    }
    VerticalSpacer()
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findDecHours.toString(),
        onValueChange = { findDecHours = it.toInt() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("DEC Hours") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findDecMinutes.toString(),
        onValueChange = { findDecMinutes = it.toInt() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("DEC Minutes") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = findDecSeconds.toString(),
        onValueChange = { findDecSeconds = it.toFloat() },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("DEC Seconds") })
    }
    VerticalSpacer()
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("GO") },
        //enabled = bluetoothConnected,
        modifier = textModifier.weight(1.0f),
        onClick = {
          onGoButton(
            arduinoCommander = arduinoCommander,
            findRaHours = findRaHours,
            findRaMinutes = findRaMinutes,
            findRaSeconds = findRaSeconds,
            findDecHours = findDecHours,
            findDecMinutes = findDecMinutes,
            findDecSeconds = findDecSeconds,
            onBackClick = onBackClick
          )
        }
      )
    }
  }
}

private fun onStarFound(applicationState: ApplicationState, arduinoCommander: ArduinoCommander, onBackClick: () -> Unit) {
  applicationState.findStar?.let {
    arduinoCommander.sendFindStar(index = it.starIndex, starTarget = it)
  }
  onBackClick()
}

private fun onGoButton(
  arduinoCommander: ArduinoCommander,
  findRaHours: Int,
  findRaMinutes: Int,
  findRaSeconds: Float,
  findDecHours: Int,
  findDecMinutes: Int,
  findDecSeconds: Float,
  onBackClick: () -> Unit
) {
  arduinoCommander.sendGo(findRaHours, findRaMinutes, findRaSeconds, findDecHours, findDecMinutes, findDecSeconds)
  onBackClick()
}

@Composable
private fun StarPicker(
  rowModifier: Modifier,
  textColumnModifier: Modifier,
  starTargets: StarTargets,
  thisTarget: () -> StarTargets.Target?,
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
        DropdownMenuItem(modifier = Modifier.fillMaxWidth(),
          enabled = true,
          text = {
            Column {
              Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxSize(0.5f), text = it.targetName)
                Text(modifier = Modifier.width(90.dp), text = it.raShort)
                Text(modifier = Modifier.width(90.dp), text = it.decShort)
              }
              SimpleStarCanvas(
                target = it,
                height = 40.dp,
                color = Color.White
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

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Standard page")
@Composable
fun FindScreenPreview() {
  GreatGrandeursTheme {
    FindScreen(
      modifier = Modifier,
      onBackClick = {},
      starTargets = StarTargets(listOf(StarTargets.Target(1, "Test", "Test", "", "10", "20"))),
      applicationState = ApplicationState(),
      arduinoCommander = object : ArduinoCommander {
        override val connected: Boolean = true
        override val arduinoSlaveMode: Boolean = true
        override val arduinoLightsOn: Boolean = true
      }
    )
  }
}
