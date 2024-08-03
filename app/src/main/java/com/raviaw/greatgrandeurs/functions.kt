package com.raviaw.greatgrandeurs

import java.text.DecimalFormat

private val formatter = DecimalFormat("0.00")
fun Double.formatCoordinate(): String {
  return formatter.format(this)
}
