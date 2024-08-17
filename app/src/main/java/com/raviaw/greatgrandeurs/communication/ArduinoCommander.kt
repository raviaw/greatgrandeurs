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
  fun sendTime() {}
  fun sendSlaveMode() {}
  fun sendMasterMode() {}
  fun sendStartCalibrating(index: Int, starTarget: StarTargets.Target) {}
  fun sendCalibratingMoveSpeed(x: Int, y: Int) {}
  fun sendCalibratingMoveStop() {}
  fun sendStoreCalibration() {}
  fun sendCalibrationCompleted() {}
}
