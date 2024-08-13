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
import com.raviaw.greatgrandeurs.tracking.StarTargets

@Composable
fun CalibrateScreen(modifier: Modifier = Modifier, starTargets: StarTargets, onBackClick: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

  var firstStarName by remember { mutableStateOf("") }
  var secondStarName by remember { mutableStateOf("") }

  var starCanvasTargets by remember { mutableStateOf<List<StarCanvasTarget>>(emptyList()) }

  LaunchedEffect(firstStarName, secondStarName) {
    val localCanvasTargets = mutableListOf<StarCanvasTarget>()

    val firstTarget = starTargets.targets.find { it.targetName == firstStarName }
    if (firstTarget != null) {
      localCanvasTargets.add(StarCanvasTarget(firstTarget.targetName, Color.Yellow, firstTarget))
    }
    val secondTarget = starTargets.targets.find { it.targetName == secondStarName }
    if (secondTarget != null) {
      localCanvasTargets.add(StarCanvasTarget(secondTarget.targetName, Color.Cyan, secondTarget))
    }

    starCanvasTargets = localCanvasTargets
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
    StarPicker(rowModifier, textColumnModifier, starTargets, Color.Yellow) { firstStarName = it }
    VerticalSpacer()
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Second Star")
    StarPicker(rowModifier, textColumnModifier, starTargets, Color.Cyan) { secondStarName = it }
    VerticalSpacer()
    VerticalSpacer()
    StarCanvas(targets = starCanvasTargets)
  }
//    Row(modifier = rowModifier) {
//      Text(
//        modifier = Modifier.weight(0.6f),
//        text = "Selected device: $selectedDeviceName"
//      )
//      Column(modifier = Modifier.weight(0.4f)) {
//        Button(
//          modifier = Modifier.fillMaxWidth(),
//          content = {
//            Text(
//              if (buttonBusy) {
//                "Busy"
//              } else if (bluetoothConnected) {
//                "Connected"
//              } else {
//                "Connect"
//              }
//            )
//          },
//          enabled = !bluetoothConnected && !buttonBusy,
//          onClick = { scope.launch(Dispatchers.IO) { connectToDevice(bluetoothCommunication.selectedDevice) } }
//        )
//        Button(
//          modifier = Modifier.fillMaxWidth(),
//          content = {
//            Text("Send time")
//          },
//          enabled = bluetoothConnected,
//          onClick = { scope.launch(Dispatchers.IO) { sendTime() } }
//        )
//      }
//    }
}

@Composable
private fun StarPicker(
  rowModifier: Modifier,
  textColumnModifier: Modifier,
  starTargets: StarTargets,
  color: Color,
  onStarSelected: (String) -> Unit
) {
  var starName by remember { mutableStateOf("") }
  var starExpanded by remember { mutableStateOf(false) }

  //
  Row(verticalAlignment = Alignment.CenterVertically, modifier = rowModifier) {
    OutlinedTextField(
      value = starName,
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
          text = {
            Column {
              Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxSize(0.5f), text = it.targetName)
                Text(modifier = Modifier.width(90.dp), text = it.raShort)
                Text(modifier = Modifier.width(90.dp), text = it.decShort)
              }
              SimpleStarCanvas(target = it, width = 50.dp, height = 40.dp, color = color)
            }
          },
          onClick = {
            starName = it.targetName
            starExpanded = false
            onStarSelected(starName)
          }
        )
      }
    }
  }
}
