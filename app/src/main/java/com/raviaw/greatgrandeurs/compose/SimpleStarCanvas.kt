//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.mapDouble
import com.raviaw.greatgrandeurs.tracking.StarTargets

@Composable
fun SimpleStarCanvas(target: StarTargets.Target, width: Dp, height: Dp, color: Color) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .defaultMinSize(minHeight = height, minWidth = width)
      .drawBehind { drawCoordinates(target = target, color = color) }
  )
}

private fun DrawScope.drawCoordinates(target: StarTargets.Target, color: Color) {
  try {
    val canvasQuadrantSize = size
    drawRect(
      color = Color.Black,
      size = canvasQuadrantSize
    )
    val raLine = mapDouble(target.raNum, 0.0, 24.0, 0.0, size.width * 1.0).toFloat()
    drawLine(color, Offset(raLine, 0.0f), Offset(raLine, size.height))
    val decLine = mapDouble(target.decNum, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
    drawLine(color, Offset(0.0f, decLine), Offset(size.width, decLine))
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}