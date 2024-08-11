//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArduinoJsonProcessor @Inject constructor(val arduinoState: ArduinoState) {
  fun processJson(jsonObject: JSONObject) {
    arduinoState.ra = jsonObject.getDouble("ra")
    arduinoState.azm = jsonObject.getDouble("azm")
    arduinoState.dec = jsonObject.getDouble("dec")
    arduinoState.alt = jsonObject.getDouble("alt")
    arduinoState.calibrated = jsonObject.getInt("calibrated")
  }
}