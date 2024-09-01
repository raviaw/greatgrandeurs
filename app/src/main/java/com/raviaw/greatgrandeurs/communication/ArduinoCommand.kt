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

  object LightsOn : ArduinoCommand() {
    override val commandName = "lights-on"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object LightsOff : ArduinoCommand() {
    override val commandName = "lights-off"

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

  object Erase : ArduinoCommand() {
    override val commandName = "erase"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class StartCalibrating(private val index: Int, private val starTarget: StarTargets.Target) : ArduinoCommand() {
    override val commandName = "c-start"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      jsonObject.put("index", index)
      jsonObject.put("starIndex", starTarget.starIndex)
      jsonObject.put("target", starTarget.targetName)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class CalibratingMoveSpeed(private val x: Int, private val y: Int, private val speed: Int) : ArduinoCommand() {
    override val commandName = "c-move"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      val multiplier = speed
      jsonObject.put("x", (x * multiplier))
      jsonObject.put("y", (y * multiplier))
      jsonObject.put("speed", speed)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object CalibratingMoveStop : ArduinoCommand() {
    override val commandName = "c-stop"

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

  object SendMenuMain : ArduinoCommand() {
    override val commandName = "menu-main"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class FindStar(private val index: Int, private val starTarget: StarTargets.Target) : ArduinoCommand() {
    override val commandName = "find-star"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      jsonObject.put("index", index)
      jsonObject.put("starIndex", starTarget.starIndex)
      jsonObject.put("target", starTarget.targetName)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object PrepareToMove : ArduinoCommand() {
    override val commandName = "move-start"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class MoveSpeed(private val x: Int, private val y: Int, private val speed: Float) : ArduinoCommand() {
    override val commandName = "move"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      val multiplier = speed
      jsonObject.put("x", (x * multiplier))
      jsonObject.put("y", (y * multiplier))
      jsonObject.put("speed", speed)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  object MoveCompleted : ArduinoCommand() {
    override val commandName = "move-done"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }

  class Go(
    private val raHours: Int,
    private val raMinutes: Int,
    private val raSeconds: Float,
    private val decHours: Int,
    private val decMinutes: Int,
    private val decSeconds: Float
  ) : ArduinoCommand() {
    override val commandName = "go"

    override fun send(bluetoothCommunication: BluetoothCommunication) {
      val jsonObject = startJsonObject()
      jsonObject.put("ra-h", raHours)
      jsonObject.put("ra-m", raMinutes)
      jsonObject.put("ra-s", raSeconds)
      jsonObject.put("dec-h", decHours)
      jsonObject.put("dec-m", decMinutes)
      jsonObject.put("dec-s", decSeconds)
      sendJsonObject(bluetoothCommunication, jsonObject)
    }
  }
}
