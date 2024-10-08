//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.VerticalSpacer

@Composable
fun MoveGridCanvas(
  modifier: Modifier = Modifier,
  layoutSize: Size?,
  activePoint: Point,
  activeColor: Color,
  boxSize: Float,
  speed: Float,
  numberOfLines: Int,
  onSpeedChange: (Float) -> Unit,
  onBoxInput: (BoxInput) -> Unit,
  onGlobalPositioned: (LayoutCoordinates) -> Unit
) {
  Column(
    modifier = modifier.fillMaxSize()
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
      Text(modifier = modifier.weight(0.2f), text = "Speed:")
      VerticalSpacer()
      Slider(
        modifier = modifier.weight(0.8f),
        value = speed,
        onValueChange = onSpeedChange,
        steps = 10,
        valueRange = 0f..10f
      )
    }
    Box(
      modifier = modifier
        .fillMaxWidth()
        .height(300.dp)
        .drawBehind {
          drawGrid(
            boxSize = boxSize,
            numberOfLines = numberOfLines,
            layoutSize = layoutSize,
            activePoint = activePoint,
            activeColor = activeColor
          )
        }
        .onGloballyPositioned { onGlobalPositioned(it) }
        .pointerInput(Unit) {
          detectDragGestures { change, dragAmount ->
            val boxInput = BoxInput(change.position.x, change.position.y)
            onBoxInput(boxInput)
            Log.d("location", "${change.position}")
          }
        }
    )
  }
}

data class Point(val x: Int, val y: Int)

data class BoxInput(val x: Float, val y: Float)

private fun DrawScope.drawGrid(boxSize: Float, numberOfLines: Int, layoutSize: Size?, activePoint: Point, activeColor: Color) {
  if (layoutSize == null) {
    return
  }

  val boxWidth = layoutSize.width / boxSize
  val boxHeight = layoutSize.height / boxSize
  try {
    val canvasQuadrantSize = size
    drawRect(
      color = activeColor,
      size = canvasQuadrantSize
    )
    for (step in 0 until numberOfLines) {
      val horizontalPos = step * boxWidth
      val verticalPos = step * boxHeight
      drawLine(Color.White, Offset(horizontalPos, 0f), Offset(horizontalPos, layoutSize.height))
      drawLine(Color.White, Offset(0f, verticalPos), Offset(layoutSize.width, verticalPos))
    }
    val leftOffset = boxWidth * activePoint.x
    val topOffset = boxHeight * activePoint.y
    drawRect(Color.Yellow, Offset(leftOffset, topOffset), Size(boxWidth, boxHeight))
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}
