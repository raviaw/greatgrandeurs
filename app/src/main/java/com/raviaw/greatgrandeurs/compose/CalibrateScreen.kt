//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.standardPadding

@Composable
fun CalibrateScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  val rowModifier = Modifier.standardPadding()

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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "First Point")
    //
    VerticalSpacer()
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
