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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.communication.FoundBluetoothDevice
import com.raviaw.greatgrandeurs.communication.IBluetoothImplementation
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(modifier: Modifier = Modifier, bluetoothCommunication: IBluetoothImplementation, onBackClick: () -> Unit) {
  val textColumnModifier = Modifier.fillMaxWidth()
  val textModifier = Modifier
    .fillMaxWidth(0.4f)

  var connectedDevice by remember { mutableStateOf<FoundBluetoothDevice?>(null) }

  fun useDevice(bluetoothDevice: FoundBluetoothDevice) {
    connectedDevice = bluetoothDevice
  }

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
    Text(text = "Make sure the bluetooth device is connected")
    Text(text = "Adapter is valid: " + (bluetoothCommunication.adapterAvailable))
    Spacer(modifier = Modifier.height(6.dp))
    //
    Row() {
      Text(
        modifier = Modifier.weight(0.4f),
        text = connectedDevice?.let {
          "Connected device: ${it.name}"
        } ?: "No connected device"
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    bluetoothCommunication.devices().orEmpty().forEach { bluetoothDevice ->
      Row(modifier = Modifier.padding(6.dp)) {
        Text(modifier = Modifier.weight(0.6f), text = "Bluetooth: ${bluetoothDevice.name}")
        Button(modifier = Modifier.weight(0.4f), content = { Text("Use device") }, onClick = { useDevice(bluetoothDevice) })
      }
    }
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Standard page")
@Composable
fun BluetoothScreenPreview() {
  GreatGrandeursTheme {
    val bluetoothCommunication = object : IBluetoothImplementation {
      override val adapterAvailable: Boolean = true

      override fun devices(): Set<FoundBluetoothDevice> = setOf(
        FoundBluetoothDevice("Test 1", "AA:AA:AA:AA:AA:00"),
        FoundBluetoothDevice("Test 2", "AA:AA:AA:AA:AA:00"),
        FoundBluetoothDevice("Test 3", "AA:AA:AA:AA:AA:00")
      )
    }
    BluetoothScreen(modifier = Modifier, onBackClick = {}, bluetoothCommunication = bluetoothCommunication)
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "No devices")
@Composable
fun BluetoothScreenNoDevicesPreview() {
  GreatGrandeursTheme {
    val bluetoothCommunication = object : IBluetoothImplementation {
      override val adapterAvailable: Boolean = false

      override fun devices(): Set<FoundBluetoothDevice> = emptySet()
    }
    BluetoothScreen(modifier = Modifier, onBackClick = {}, bluetoothCommunication = bluetoothCommunication)
  }
}