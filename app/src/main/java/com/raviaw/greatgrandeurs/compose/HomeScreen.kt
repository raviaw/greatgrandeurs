//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.HorizontalSpacer
import com.raviaw.greatgrandeurs.StandardPadding
import com.raviaw.greatgrandeurs.VerticalSpacer
import com.raviaw.greatgrandeurs.communication.ArduinoState
import com.raviaw.greatgrandeurs.communication.IBluetoothImplementation
import com.raviaw.greatgrandeurs.formatCoordinate
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  arduinoState: ArduinoState,
  bluetoothCommunication: IBluetoothImplementation,
  onCalibrate: () -> Unit,
  onMove: () -> Unit,
  onFind: () -> Unit,
  onReport: () -> Unit,
  onBluetooth: () -> Unit,
  onSettings: () -> Unit,
) {
  var rightAscension by remember { mutableDoubleStateOf(0.0) }
  var declination by remember { mutableDoubleStateOf(0.0) }
  var altitude by remember { mutableDoubleStateOf(0.0) }
  var azimuth by remember { mutableDoubleStateOf(0.0) }

  var selectedDeviceName by remember { mutableStateOf("No device") }
  var bluetoothConnected by remember { mutableStateOf(false) }
  
  val textColumnModifier = Modifier
    .fillMaxWidth()
    .padding(StandardPadding)
  val textModifier = Modifier
    .fillMaxWidth(0.4f)
  val basePadding = Modifier.padding(StandardPadding)

  LaunchedEffect(Unit) {
    while (true) {
      delay(500.milliseconds)
      selectedDeviceName = bluetoothCommunication.selectedDevice?.name ?: "No device"
      bluetoothConnected = bluetoothCommunication.bluetoothConnection != null

      rightAscension = arduinoState.ra
      declination = arduinoState.dec
      altitude = arduinoState.alt
      azimuth = arduinoState.azm
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
    Text(modifier = basePadding, style = MaterialTheme.typography.headlineMedium, text = "Great Grandeurs")
    Text(modifier = basePadding, style = MaterialTheme.typography.headlineSmall, text = "Positioning")
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = rightAscension.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("RA") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = declination.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("DEC") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = altitude.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("ALT") })
      HorizontalSpacer()
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = azimuth.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("AZM") })
    }
    VerticalSpacer()
    Text(modifier = basePadding, style = MaterialTheme.typography.headlineSmall, text = "Options")
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Calibrate") },
        enabled = bluetoothConnected,
        modifier = textModifier.weight(1.0f),
        onClick = { onCalibrate() }
      )
      HorizontalSpacer()
      Button(
        content = { Text("Move") },
        enabled = bluetoothConnected,
        modifier = textModifier.weight(1.0f),
        onClick = { onMove() }
      )
      HorizontalSpacer()
      Button(
        content = { Text("Find") },
        enabled = bluetoothConnected,
        modifier = textModifier.weight(1.0f),
        onClick = { onFind() }
      )
    }
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Report") },
        enabled = bluetoothConnected,
        modifier = textModifier.weight(1.0f),
        onClick = { onReport() }
      )
      HorizontalSpacer()
      Button(
        content = { Text("Bluetooth") },
        modifier = textModifier.weight(1.0f),
        onClick = { onBluetooth() }
      )
      HorizontalSpacer()
      Button(
        content = { Text("Settings") },
        modifier = textModifier.weight(1.0f),
        onClick = { onSettings() }
      )
    }
    VerticalSpacer()
    Text(modifier = basePadding, style = MaterialTheme.typography.headlineSmall, text = "Coordinates")
    Box(
      contentAlignment = Alignment.Center,
      modifier = basePadding.fillMaxSize()
    ) {
      CoordinatesCanvas(arduinoState = arduinoState)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  GreatGrandeursTheme {
    HomeScreen(
      modifier = Modifier,
      arduinoState = ArduinoState(),
      bluetoothCommunication = IBluetoothImplementation.EMPTY,
      onCalibrate = {},
      onMove = {},
      onFind = {},
      onReport = {},
      onBluetooth = {},
      onSettings = {})
  }
}