package com.raviaw.greatgrandeurs

import java.text.DecimalFormat

private val formatter = DecimalFormat("0.00")

fun Double.formatCoordinate(): String {
  return formatter.format(this)
}

fun mapDouble(input: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
  return (input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}
