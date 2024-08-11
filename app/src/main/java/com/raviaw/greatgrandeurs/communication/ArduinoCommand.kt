//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

sealed class ArduinoCommand() {
  abstract val commandName: String
  abstract fun send(bluetoothCommunication: BluetoothCommunication)

  protected fun startJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("command", commandName)
    return jsonObject
  }

  protected fun sendJsonObject(bluetoothCommunication: BluetoothCommunication, jsonObject: JSONObject) {
    val stringValue = jsonObject.toString()
    Log.d(TAG, "Writing json object $stringValue")
    bluetoothCommunication.writeToCurrentDevice(stringValue.encodeToByteArray())
  }

  object SendTime : ArduinoCommand() {
    override val commandName = "time"
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      val formattedTime = dateTimeFormat.format(Date())
      // 2023-01-01 13:00:00.000
      jsonObject.put("time", formattedTime)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }
}