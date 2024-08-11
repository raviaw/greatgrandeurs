//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.communication.BluetoothConnection
import com.raviaw.greatgrandeurs.communication.FoundBluetoothDevice
import com.raviaw.greatgrandeurs.communication.IBluetoothImplementation
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(modifier: Modifier = Modifier, bluetoothCommunication: IBluetoothImplementation, onBackClick: () -> Unit) {
  var displayMessage by remember { mutableStateOf("No error") }
  var lastBluetoothText by remember { mutableStateOf("") }
  var bluetoothTextCount by remember { mutableIntStateOf(0) }
  var selectedDeviceName by remember { mutableStateOf("No device") }
  var bluetoothConnected by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    while (true) {
      delay(500.milliseconds)
      selectedDeviceName = bluetoothCommunication.selectedDevice?.name ?: "No device"
      bluetoothConnected = bluetoothCommunication.bluetoothConnection != null
      bluetoothCommunication.bluetoothConnection?.let {
        try {
          displayMessage = "Listening to device [${it.name}]"
          lastBluetoothText = it.lastLine ?: "No message"
          bluetoothTextCount = it.reads
        } catch (ex: Exception) {
          Log.e(TAG, "Error listening to device ${it.name}", ex)
          displayMessage = "Error listening to device ${it.name}: ${ex.message}"
        }
      } ?: let {
        lastBluetoothText = "No connection"
        bluetoothTextCount = 0
      }
    }
  }

  fun useDevice(bluetoothDevice: FoundBluetoothDevice) {
    bluetoothCommunication.selectedDevice = bluetoothDevice
  }

  fun connectToDevice(bluetoothDevice: FoundBluetoothDevice?) {
    displayMessage = "Connecting to device..."
    try {
      if (bluetoothDevice == null) {
        error("No device selected")
      }
      displayMessage = "Connected to device [${bluetoothDevice.nativeDevice}]"
      bluetoothCommunication.connectToDevice(bluetoothDevice)
    } catch (ex: Exception) {
      Log.e(TAG, "Error listening to device $bluetoothDevice", ex)
      displayMessage = "Error listening to device $bluetoothDevice: ${ex.message}"
    }
  }

  fun sendTime() {
    displayMessage = "Sending time..."
    try {
      bluetoothCommunication.sendTime()
    } catch (ex: Exception) {
      Log.e(TAG, "Error sending time", ex)
      displayMessage = "Error sending time: ${ex.message}"
    }
  }

  val rowModifier = Modifier.padding(6.dp)

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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineMedium, text = "Bluetooth")
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Current device")
    Column(modifier = rowModifier) {
      Text(text = "Make sure the bluetooth device is connected")
      Text(text = "Adapter is valid: " + (bluetoothCommunication.adapterAvailable))
    }
    //
    Spacer(modifier = Modifier.height(6.dp))
    Row(modifier = rowModifier) {
      Text(
        modifier = Modifier.weight(0.6f),
        text = "Selected device: $selectedDeviceName"
      )
      Column(modifier = Modifier.weight(0.4f)) {
        Button(
          modifier = Modifier.fillMaxWidth(),
          content = {
            Text(
              if (bluetoothConnected) {
                "Connected"
              } else {
                "Connect"
              }
            )
          },
          enabled = !bluetoothConnected,
          onClick = { scope.launch(Dispatchers.IO) { connectToDevice(bluetoothCommunication.selectedDevice) } }
        )
        Button(
          modifier = Modifier.fillMaxWidth(),
          content = {
            Text("Send time")
          },
          enabled = bluetoothConnected,
          onClick = { scope.launch(Dispatchers.IO) { sendTime() } }
        )
      }
    }

    //
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Message information")
    Spacer(modifier = Modifier.height(6.dp))
    Text(modifier = rowModifier, text = displayMessage)

    //
    Spacer(modifier = Modifier.height(6.dp))
    Row(modifier = rowModifier) {
      Text(
        modifier = Modifier.weight(0.2f),
        text = bluetoothTextCount.toString()
      )
      Text(
        modifier = Modifier.weight(0.8f),
        text = lastBluetoothText
      )
    }

//    
    Spacer(modifier = Modifier.height(6.dp))
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Devices")
    bluetoothCommunication.devices().orEmpty().forEach { bluetoothDevice ->
      Row(modifier = Modifier.padding(6.dp)) {
        Text(modifier = Modifier.weight(0.6f), text = "Bluetooth: ${bluetoothDevice.name}")
        Button(
          modifier = Modifier.weight(0.4f),
          content = { Text("Use device") },
          onClick = { useDevice(bluetoothDevice) },
          enabled = !bluetoothConnected
        )
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
      override var selectedDevice: FoundBluetoothDevice? = null
      override var bluetoothConnection: BluetoothConnection? = null

      override fun devices(): Set<FoundBluetoothDevice> = setOf(
        FoundBluetoothDevice("Test 1", "AA:AA:AA:AA:AA:00", null),
        FoundBluetoothDevice("Test 2", "AA:AA:AA:AA:AA:00", null),
        FoundBluetoothDevice("Test 3", "AA:AA:AA:AA:AA:00", null)
      )

      override fun connectToDevice(device: FoundBluetoothDevice) {
        throw UnsupportedOperationException()
      }

      override fun sendTime() {
        throw UnsupportedOperationException()
      }
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
      override var selectedDevice: FoundBluetoothDevice? = null
      override var bluetoothConnection: BluetoothConnection? = null

      override fun devices(): Set<FoundBluetoothDevice> = emptySet()

      override fun connectToDevice(device: FoundBluetoothDevice) {
        throw UnsupportedOperationException()
      }

      override fun sendTime() {
        throw UnsupportedOperationException()
      }
    }
    BluetoothScreen(modifier = Modifier, onBackClick = {}, bluetoothCommunication = bluetoothCommunication)
  }
}