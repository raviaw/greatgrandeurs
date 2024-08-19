//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import com.raviaw.greatgrandeurs.tracking.StarTargets

interface ArduinoCommander {
  val connected: Boolean
  val arduinoSlaveMode: Boolean
  fun sendTime() {}
  fun sendArduinoSlaveMode() {}
  fun sendArduinoFreeMode() {}
  fun sendStartCalibrating(index: Int, starTarget: StarTargets.Target) {}
  fun sendCalibratingMoveSpeed(x: Int, y: Int) {}
  fun sendCalibratingMoveStop() {}
  fun sendStoreCalibration() {}
  fun sendCalibrationCompleted() {}
}
