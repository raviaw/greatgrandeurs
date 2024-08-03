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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.formatCoordinate
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onCalibrate: () -> Unit, onMove: () -> Unit, onFind: () -> Unit, onReport: () -> Unit) {
  val azimuth by remember { mutableDoubleStateOf(0.0) }
  val altitude by remember { mutableDoubleStateOf(0.0) }
  val rightAscension by remember { mutableDoubleStateOf(0.0) }
  val declination by remember { mutableDoubleStateOf(0.0) }

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
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = azimuth.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("Azimuth") })
      Spacer(modifier = Modifier.width(6.dp))
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = altitude.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("Altitude") })
    }
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = rightAscension.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("Right Ascension") })
      Spacer(modifier = Modifier.width(6.dp))
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = declination.formatCoordinate(),
        onValueChange = {},
        readOnly = true,
        label = { Text("Declination") })
    }
    Spacer(modifier = Modifier.height(6.dp))
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Calibrate") },
        modifier = textModifier.weight(1.0f),
        onClick = { onCalibrate() }
      )
      Spacer(modifier = Modifier.width(6.dp))
      Button(
        content = { Text("Move") },
        modifier = textModifier.weight(1.0f),
        onClick = { onMove() }
      )
    }
    Row(verticalAlignment = Alignment.Top, modifier = textColumnModifier) {
      Button(
        content = { Text("Find") },
        modifier = textModifier.weight(1.0f),
        onClick = { onFind() }
      )
      Spacer(modifier = Modifier.width(6.dp))
      Button(
        content = { Text("Report") },
        modifier = textModifier.weight(1.0f),
        onClick = { onReport() }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  GreatGrandeursTheme {
    HomeScreen(modifier = Modifier, onCalibrate = {}, onMove = {}, onFind = {}, onReport = {})
  }
}