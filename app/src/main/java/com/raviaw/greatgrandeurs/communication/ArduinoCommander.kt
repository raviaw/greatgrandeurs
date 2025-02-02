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
  val arduinoLightsOn: Boolean
  val laserOn: Boolean
  fun sendTime() {}
  fun sendErase() {}
  fun sendArduinoSlaveMode() {}
  fun sendArduinoFreeMode() {}
  fun sendLightsOn() {}
  fun sendLightsOff() {}
  fun sendFindStar(index: Int, starTarget: StarTargets.Target) {}
  fun sendStartCalibrating(index: Int, starTarget: StarTargets.Target) {}
  fun sendCalibratingMoveSpeed(x: Int, y: Int, speed: Float) {}
  fun sendGo(raHours: Int, raMinutes: Int, raSeconds: Float, decHours: Int, decMinutes: Int, decSeconds: Float) {}
  fun sendCalibratingMoveStop() {}
  fun sendStoreCalibration() {}
  fun sendCalibrationCompleted() {}
  fun sendPrepareToMove() {}
  fun sendMoveSpeed(x: Int, y: Int, speed: Float) {}
  fun sendMoveCompleted() {}
  fun sendMenuMain() {}
  fun sendMeasureBackslash() {}
  fun sendLaserOn() {}
  fun sendLaserOff() {}
}
