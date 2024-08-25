package com.raviaw.greatgrandeurs

import java.text.DecimalFormat

private val formatter = DecimalFormat("0.00")

fun Double.formatCoordinate(): String {
  return formatter.format(this)
}

fun mapDouble(input: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
  return (input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun mapFloat(input: Float, inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
  return (input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun mapInt(input: Int, inMin: Int, inMax: Int, outMin: Int, outMax: Int): Int {
  return (input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun withinBoundaries(value: Int, min: Int, max: Int): Int {
  return when {
    value < min -> min
    value > max -> max
    else -> value
  }
}

