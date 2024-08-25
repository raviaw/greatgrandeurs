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

    try {
      arduinoState.ra = jsonObject.getDouble("ra")
      arduinoState.dec = jsonObject.getDouble("dec")
      arduinoState.lastStarRa = jsonObject.getDouble("last-star-ra")
      arduinoState.lastStarDec = jsonObject.getDouble("last-star-dec")
      arduinoState.azm = jsonObject.getDouble("azm")
      arduinoState.alt = jsonObject.getDouble("alt")
      arduinoState.currentMotorAzm = jsonObject.getDouble("c-m-azm")
      arduinoState.currentMotorAlt = jsonObject.getDouble("c-m-alt")
      arduinoState.calibrated = jsonObject.getInt("calibrated") != 0
      arduinoState.activeMode = ArduinoMode.lookupValue(jsonObject.getInt("activeMode"))
      arduinoState.horizontalMotorPosition = jsonObject.getLong("hor-motor")
      arduinoState.verticalMotorPosition = jsonObject.getLong("ver-motor")
      arduinoState.horizontalEncoderPosition = jsonObject.getLong("hor-encoder")
      arduinoState.verticalEncoderPosition = jsonObject.getLong("ver-encoder")

      if (arduinoState.lastStarRa < -0) {
        arduinoState.lastStarRa = 12.0
      }
      if (arduinoState.lastStarDec < -90) {
        arduinoState.lastStarDec = 0.0
      }
    } catch (ex: Exception) {
      Log.w(TAG, "Error processing input from Android", ex)
    }
  }
}
