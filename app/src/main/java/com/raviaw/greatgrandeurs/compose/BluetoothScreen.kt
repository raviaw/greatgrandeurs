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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.FoundBluetoothDevice
import com.raviaw.greatgrandeurs.communication.IBluetoothImplementation
import com.raviaw.greatgrandeurs.standardHorizontalPadding
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(
  modifier: Modifier = Modifier,
  applicationState: ApplicationState,
  bluetoothCommunication: IBluetoothImplementation,
  onBackClick: () -> Unit
) {
  var displayMessage by remember { mutableStateOf("No error") }
  var lastBluetoothText by remember { mutableStateOf("{last bluetooth message}") }
  var bluetoothTextCount by remember { mutableLongStateOf(0) }
  var selectedDeviceName by remember { mutableStateOf("No device") }
  var bluetoothConnected by remember { mutableStateOf(false) }
  var buttonBusy by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    while (true) {
      delay(500.milliseconds)
      selectedDeviceName = applicationState.bluetoothState.selectedDevice?.name ?: "No device"
      bluetoothConnected = applicationState.bluetoothState.bluetoothConnection != null
      applicationState.bluetoothState.bluetoothConnection?.let {
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
    applicationState.bluetoothState.selectedDevice = bluetoothDevice
  }

  fun connectToDevice(bluetoothDevice: FoundBluetoothDevice?) {
    buttonBusy = true
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
    } finally {
      buttonBusy = false
    }
  }

  fun disconnectFromCurrentDevice() {
    buttonBusy = true
    displayMessage = "Disconnecting from device..."
    try {
      displayMessage = "Disconnected from current device"
      bluetoothCommunication.disconnectFromCurrentDevice()
    } catch (ex: Exception) {
      Log.e(TAG, "Error disconnecting from current device", ex)
      displayMessage = "Error disconnecting from current device: ${ex.message}"
    } finally {
      buttonBusy = false
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

  fun sendErase() {
    displayMessage = "Sending erase..."
    try {
      bluetoothCommunication.sendErase()
    } catch (ex: Exception) {
      Log.e(TAG, "Error sending erase", ex)
      displayMessage = "Error sending erase: ${ex.message}"
    }
  }

  fun sendMeasureBackslash() {
    displayMessage = "Sending measure backslash..."
    try {
      bluetoothCommunication.sendMeasureBackslash()
    } catch (ex: Exception) {
      Log.e(TAG, "Error sending measure backslash", ex)
      displayMessage = "Error sending measure backslash: ${ex.message}"
    }
  }

  val rowModifier = Modifier.standardPadding()

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
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineMedium, text = "Bluetooth")
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Current device")
    Column(modifier = rowModifier) {
      Text(text = "Make sure the bluetooth device is connected")
      Text(
        text = if (bluetoothCommunication.adapterAvailable) {
          "Bluetooth adapter found"
        } else {
          "No bluetooth available"
        }
      )
    }
    //
    Row(modifier = rowModifier, verticalAlignment = Alignment.Top) {
      Column(modifier = Modifier.weight(0.4f)) {
        Text(
          text = "Selected device:"
        )
        VerticalSpacer()
        Text(
          text = selectedDeviceName
        )
        //
        Text(text = displayMessage)
        Text(
          text = bluetoothTextCount.toString()
        )
      }
      HorizontalSpacer()
      Column(modifier = Modifier.weight(0.6f)) {
        Button(
          modifier = Modifier.fillMaxWidth(),
          content = {
            Text(
              if (buttonBusy) {
                "Busy"
              } else if (bluetoothConnected) {
                "Disconnect"
              } else {
                "Connect"
              }
            )
          },
          enabled = !buttonBusy,
          onClick = {
            scope.launch(Dispatchers.IO) {
              if (!bluetoothConnected) {
                connectToDevice(applicationState.bluetoothState.selectedDevice)
              } else {
                disconnectFromCurrentDevice()
              }
            }
          }
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          Button(
            content = {
              Text("Time")
            },
            modifier = Modifier.weight(0.5f),
            enabled = bluetoothConnected,
            onClick = { scope.launch(Dispatchers.IO) { sendTime() } }
          )
          HorizontalSpacer()
          Button(
            content = {
              if (bluetoothCommunication.arduinoSlaveMode) {
                Text("Free")
              } else {
                Text("Slave")
              }
            },
            modifier = Modifier.weight(0.5f),
            enabled = bluetoothConnected,
            onClick = {
              scope.launch(Dispatchers.IO) {
                if (bluetoothCommunication.arduinoSlaveMode) {
                  bluetoothCommunication.sendArduinoFreeMode()
                } else {
                  bluetoothCommunication.sendArduinoSlaveMode()
                }
              }
            }
          )
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          Button(
            modifier = Modifier.weight(0.5f),
            content = {
              if (bluetoothCommunication.arduinoLightsOn) {
                Text("Lg. Off")
              } else {
                Text("Lg. On")
              }
            },
            enabled = bluetoothConnected,
            onClick = {
              scope.launch(Dispatchers.IO) {
                if (bluetoothCommunication.arduinoLightsOn) {
                  bluetoothCommunication.sendLightsOff()
                } else {
                  bluetoothCommunication.sendLightsOn()
                }
              }
            })
          HorizontalSpacer()
          Button(
            content = {
              Text("Erase")
            },
            modifier = Modifier.weight(0.5f),
            enabled = bluetoothConnected,
            onClick = { scope.launch(Dispatchers.IO) { sendErase() } }
          )
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
        ) {
          Button(
            content = {
              Text("B. Slash")
            },
            modifier = Modifier.weight(0.5f),
            enabled = bluetoothConnected,
            onClick = { scope.launch(Dispatchers.IO) { sendMeasureBackslash() } }
          )
          HorizontalSpacer()
          Button(
            modifier = Modifier.weight(0.5f),
            content = {
              if (bluetoothCommunication.laserOn) {
                Text("Ls. Off")
              } else {
                Text("Ls. On")
              }
            },
            enabled = bluetoothConnected,
            onClick = {
              scope.launch(Dispatchers.IO) {
                if (bluetoothCommunication.laserOn) {
                  bluetoothCommunication.sendLaserOff()
                } else {
                  bluetoothCommunication.sendLaserOn()
                }
              }
            })
        }
      }
    }

    //
    Row(modifier = rowModifier) {
      Text(
        text = lastBluetoothText,
        modifier = Modifier.weight(0.8f),
        fontSize = 10.sp,
      )
    }

//
    Text(modifier = rowModifier, style = MaterialTheme.typography.headlineSmall, text = "Devices")
    bluetoothCommunication.devices().orEmpty().forEach { bluetoothDevice ->
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.standardHorizontalPadding()) {
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
      override val connected: Boolean = false
      override val arduinoSlaveMode: Boolean = false
      override val arduinoLightsOn: Boolean = false
      override val laserOn: Boolean = false

      override fun devices(): Set<FoundBluetoothDevice> = setOf(
        FoundBluetoothDevice("Test 1", "AA:AA:AA:AA:AA:00", null),
        FoundBluetoothDevice("Test 2", "AA:AA:AA:AA:AA:00", null),
        FoundBluetoothDevice("Test 3", "AA:AA:AA:AA:AA:00", null)
      )
    }
    BluetoothScreen(modifier = Modifier, onBackClick = {}, bluetoothCommunication = bluetoothCommunication, applicationState = ApplicationState())
  }
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "No devices")
@Composable
fun BluetoothScreenNoDevicesPreview() {
  GreatGrandeursTheme {
    val bluetoothCommunication = IBluetoothImplementation.EMPTY
    BluetoothScreen(modifier = Modifier, onBackClick = {}, bluetoothCommunication = bluetoothCommunication, applicationState = ApplicationState())
  }
}
