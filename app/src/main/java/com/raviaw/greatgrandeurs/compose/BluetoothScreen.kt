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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.communication.BluetoothCommunication

@Composable
fun BluetoothScreen(modifier: Modifier = Modifier, bluetoothCommunication: BluetoothCommunication, onBackClick: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  Column(
    modifier = Modifier
      .statusBarsPadding()
      .verticalScroll(rememberScrollState())
      .fillMaxSize()
      .padding(6.dp)
      .safeDrawingPadding(),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top
  ) {
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Card() {
        Text(text = "Make sure the bluetooth device is connected")
      }
    }
  }
}
