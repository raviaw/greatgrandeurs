//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.compose

import android.icu.text.DecimalFormat
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.mapDouble
import com.raviaw.greatgrandeurs.tracking.StarTargets

fun DrawScope.prepareArea() {
  try {
    val canvasQuadrantSize = size
    drawRect(
      color = Color.Black,
      size = canvasQuadrantSize
    )
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

fun DrawScope.drawTargetCoordinates(target: StarTargets.Target, color: Color) {
  try {
    drawRaDecCoordinates(ra = target.raNum, dec = target.decNum, color = color)
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

fun DrawScope.drawRaDecCoordinates(
  ra: Double,
  dec: Double,
  targetName: String? = null,
  includeCaption: Boolean = false,
  textMeasurer: TextMeasurer? = null,
  color: Color
) {
  try {
    val decimalFormatter = DecimalFormat("#.00")
    val verticalLine = mapDouble(ra, 0.0, 24.0, 0.0, size.width * 1.0).toFloat()
    val horizontalLine = mapDouble(dec, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
    drawInGrid(
      verticalLine = verticalLine,
      horizontalLine = horizontalLine,
      verticalCaption = "$targetName RA: ${decimalFormatter.format(ra)}",
      horizontalCaption = "$targetName DEC: ${decimalFormatter.format(dec)}",
      includeCaption = includeCaption,
      textMeasurer = textMeasurer,
      color = color
    )
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

fun DrawScope.drawAzmAltCoordinates(
  azm: Double,
  alt: Double,
  targetName: String? = null,
  includeCaption: Boolean = false,
  textMeasurer: TextMeasurer? = null,
  color: Color
) {
  try {
    val decimalFormatter = DecimalFormat("#.##")
    val verticalLine = mapDouble(azm, 0.0, 360.0, 0.0, size.width * 1.0).toFloat()
    val horizontalLine = mapDouble(alt, -90.0, +90.0, 0.0, size.height * 1.0).toFloat()
    drawInGrid(
      verticalLine = verticalLine,
      horizontalLine = horizontalLine,
      verticalCaption = "$targetName AZM: ${decimalFormatter.format(azm)}",
      horizontalCaption = "$targetName ALT: ${decimalFormatter.format(alt)}",
      includeCaption = includeCaption,
      textMeasurer = textMeasurer,
      color = color
    )
  } catch (ex: Exception) {
    Log.d(TAG, "Failed to draw", ex)
  }
}

fun DrawScope.drawInGrid(
  verticalLine: Float,
  horizontalLine: Float,
  verticalCaption: String? = null,
  horizontalCaption: String? = null,
  includeCaption: Boolean = false,
  textMeasurer: TextMeasurer? = null,
  color: Color
) {
  val useTextMeasurer = if (includeCaption) {
    requireNotNull(textMeasurer)
  } else {
    null
  }

  drawLine(color, Offset(verticalLine, 0.0f), Offset(verticalLine, size.height))
  if (useTextMeasurer != null && verticalCaption != null) {
    drawText(
      textMeasurer = useTextMeasurer,
      text = verticalCaption,
      style = TextStyle(
        fontSize = 6.sp,
        color = color,
        background = Color.Black
      ),
      topLeft = Offset(verticalLine, 0.0f).plus(Offset(5f, 0.0f))
    )
  }

  val useHorizontalLine = size.height - horizontalLine
  drawLine(color, Offset(0.0f, useHorizontalLine), Offset(size.width, useHorizontalLine))
  if (useTextMeasurer != null && horizontalCaption != null) {
    drawText(
      textMeasurer = useTextMeasurer,
      text = horizontalCaption,
      style = TextStyle(
        fontSize = 6.sp,
        color = color,
        background = Color.Black
      ),
      topLeft = Offset(0.0f, useHorizontalLine).plus(Offset(5f, 5f))
    )
  }
}
