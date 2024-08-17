//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import com.raviaw.greatgrandeurs.tracking.StarTargets
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

  object MasterMode : ArduinoCommand() {
    override val commandName = "master"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object SlaveMode : ArduinoCommand() {
    override val commandName = "slave"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object Time : ArduinoCommand() {
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

  class StartCalibrating(private val index: Int, private val starTarget: StarTargets.Target) : ArduinoCommand() {
    override val commandName = "c-start"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      jsonObject.put("index", index)
      jsonObject.put("target", starTarget.targetName)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class CalibratingMoveSpeed(private val x: Int, private val y: Int) : ArduinoCommand() {
    override val commandName = "c-move"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      jsonObject.put("x", x)
      jsonObject.put("y", y)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object CalibratingMoveStop : ArduinoCommand() {
    override val commandName = "stop"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object StoreCalibration : ArduinoCommand() {
    override val commandName = "c-save"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object CalibrationCompleted : ArduinoCommand() {
    override val commandName = "c-done"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }
}
