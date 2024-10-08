//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.state

import com.raviaw.greatgrandeurs.communication.ArduinoMode

class ArduinoState constructor() {
  var activeMode: ArduinoMode = ArduinoMode.MODE_MENU
  var calibrated: Boolean = false
  var ra: Double = 12.0
  var dec: Double = 0.0
  var lastStarRa: Double = 12.0
  var lastStarDec: Double = 0.0
  var alt: Double = 0.0
  var azm: Double = 180.0
  var currentMotorAlt: Double = 0.0
  var currentMotorAzm: Double = 180.0
  var horizontalMotorPosition: Long = 0L
  var verticalMotorPosition: Long = 0L
  var horizontalEncoderPosition: Long = 0L
  var verticalEncoderPosition: Long = 0L
  var accX: Double = 0.0
  var accY: Double = 0.0
  var accZ: Double = 0.0
}
