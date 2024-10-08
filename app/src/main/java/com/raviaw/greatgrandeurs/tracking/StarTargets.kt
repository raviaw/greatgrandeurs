//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.tracking

import android.content.Context
import com.raviaw.greatgrandeurs.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject

class StarTargets constructor(val targets: List<Target>) {
  @Inject
  constructor(@ApplicationContext context: Context) : this(readTargetsFromContext(context))

  class Target(
    val starIndex: Int,
    val target: String,
    val targetName: String,
    val special: String,
    val ra: String,
    val dec: String
  ) {
    val raShort = ra.split(",").joinToString(",") { it.substringBefore(".") }
    val decShort = dec.split(",").joinToString(",") { it.substringBefore(".") }
    val raNum = ra.substringBefore(",").toDouble()
    val decNum = dec.substringBefore(",").toDouble()

    companion object {
      fun fromString(starIndex: Int, str: String): Target {
        val target = str.substringAfter("target").substringBefore("=").trim()
        val data = str.substringAfter("{").substringBefore("}")
        val targetName = data.substringBefore(",").trim().removePrefix("\"").removeSuffix("\"")
        val special = data.substringAfterLast(",").trim().removePrefix("\"").removeSuffix("\"")
        val commas = data.count { it == ',' }
        lateinit var ra: String
        lateinit var dec: String
        if (commas == 7) {
          ra = data.substringAfter("Ephemeris::hoursMinutesSecondsToFloatingHours(").substringBefore(")")
          dec = data.substringAfterLast("Ephemeris::hoursMinutesSecondsToFloatingHours(").substringBefore(")")
        } else {
          ra = "0"
          dec = "0"
        }

        return Target(starIndex = starIndex, target = target, targetName = targetName, special = special, ra = ra, dec = dec)
      }
    }
  }

  companion object {
    private fun readTargetsFromContext(context: Context): List<Target> {
      val lines = InputStreamReader(context.resources.openRawResource(R.raw.stars)).use {
        it.readLines()
      }
      return lines.withIndex().map { Target.fromString(starIndex = it.index, str = it.value) }
    }
  }
}
