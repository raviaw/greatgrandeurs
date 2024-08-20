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
}
