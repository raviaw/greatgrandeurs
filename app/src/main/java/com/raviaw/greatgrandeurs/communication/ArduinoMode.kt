//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

enum class ArduinoMode(val no: Int) {
  MODE_MENU(0),
  MODE_REPORT(1),
  MODE_MOVE_MENU(2),
  MODE_CALIBRATING(3),
  MODE_FIND(4),
  MODE_AZ_ALT(5),
  MODE_MOVE_COORDINATES(7),
  MODE_MOVE_MOTOR(8),
  MODE_CALIBRATE_MOVING(10),
  MODE_CALIBRATE_STAR_COMPLETE(11),
  MODE_CALIBRATION_COMPLETE(12),
  MODE_MEASURING_BACKSLASH(13),
  MODE_UNKNOWN(-1);

  companion object {
    private val map = entries.associateBy { it.no }

    fun lookupValue(value: Int): ArduinoMode {
      return map.getOrElse(value) { MODE_UNKNOWN }
    }
  }
}
