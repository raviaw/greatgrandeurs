//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArduinoState @Inject constructor() {
  var ra: Double = 12.0
  var dec: Double = 0.0
  var alt: Double = 0.0
  var azm: Double = 180.0
  var calibrated: Int = 0
}