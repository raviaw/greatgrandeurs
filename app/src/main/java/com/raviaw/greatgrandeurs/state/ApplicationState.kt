//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.state

import com.raviaw.greatgrandeurs.tracking.StarTargets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationState @Inject constructor() {
  val arduinoState: ArduinoState = ArduinoState()
  val bluetoothState: BluetoothState = BluetoothState()
  val calibrationState: CalibrationState = CalibrationState()
  var findStar: StarTargets.Target? = null
}
