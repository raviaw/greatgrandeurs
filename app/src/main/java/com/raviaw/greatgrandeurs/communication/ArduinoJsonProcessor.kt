//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.state.ApplicationState
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArduinoJsonProcessor @Inject constructor(applicationState: ApplicationState) {
  private val arduinoState = applicationState.arduinoState

  fun processJson(jsonObject: JSONObject) {
    Log.d(TAG, "Processing object: $jsonObject")

    arduinoState.ra = jsonObject.getDouble("ra")
    arduinoState.azm = jsonObject.getDouble("azm")
    arduinoState.dec = jsonObject.getDouble("dec")
    arduinoState.alt = jsonObject.getDouble("alt")
    arduinoState.calibrated = jsonObject.getInt("calibrated")
  }
}
