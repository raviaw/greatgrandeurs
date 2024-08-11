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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalibrateScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
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
        value = "Test",
        onValueChange = {},
        readOnly = true,
        label = { Text("Azimuth") })
      Spacer(modifier = Modifier.width(6.dp))
      OutlinedTextField(
        modifier = textModifier.weight(1.0f),
        value = "Test",
        onValueChange = {},
        readOnly = true,
        label = { Text("Altitude") })
    }
  }
}
