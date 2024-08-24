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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.mapDouble
import com.raviaw.greatgrandeurs.standardPadding
import com.raviaw.greatgrandeurs.tracking.StarTargets
import com.raviaw.greatgrandeurs.ui.theme.GreatGrandeursTheme

@Composable
fun SimpleStarCanvas(target: StarTargets.Target, height: Dp, color: Color) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .defaultMinSize(minHeight = height)
      .drawBehind { drawCoordinates(target = target, color = color) }
  )
}

private fun DrawScope.drawCoordinates(target: StarTargets.Target, color: Color) {
  prepareArea()
  drawTargetCoordinates(target, color)
}

@SuppressLint("MissingPermission")
@Preview(showBackground = true, name = "Move controls dialog")
@Composable
fun SimpleStarCanvasPreview() {
  GreatGrandeursTheme {
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
      SimpleStarCanvas(StarTargets.Target(
        starIndex = 0,
        target = "Test",
        targetName = "Test",
        special = "",
        ra = "10",
        dec = "40"
      ), 40.dp, Color.Cyan)
    }
  }
}
