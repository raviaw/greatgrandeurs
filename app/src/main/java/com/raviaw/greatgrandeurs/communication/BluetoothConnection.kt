//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import org.json.JSONObject
import java.io.BufferedOutputStream
import kotlin.math.min

class BluetoothConnection(val arduinoJsonProcessor: ArduinoJsonProcessor, val device: FoundBluetoothDevice, val socket: BluetoothSocket) {
  val buffer = ByteArray(1024)
  var pointer = 0
  val inputSocket = socket.inputStream
  val outputSocket = BufferedOutputStream(socket.outputStream)
  var reads: Int = 0
  var lastLine: String? = null

  val name = device.name

  fun readNextObject(): JSONObject? {
    Log.d(TAG, "Reading next lines from device: $device")
    val readNow = inputSocket.read(buffer, pointer, buffer.size - pointer)
    pointer += readNow
    val lastPos = min(buffer.size, pointer)
    if (lastPos == 0) {
      Log.d(TAG, "No input from arduino")
      return null
    }

    //
    var endSeqPos: Int = -1
    for (pos in (lastPos - 1) downTo 0) {
      if (buffer[pos] == endSeq) {
        endSeqPos = pos
        break
      }
    }
    if (endSeqPos == -1 || endSeqPos == 0) {
      Log.d(TAG, "No valid endpos received from arduino")
      return null
    }

    //
    var startSeqPos: Int = -1
    for (pos in (endSeqPos - 1) downTo 0) {
      if (buffer[pos] == startSeq) {
        startSeqPos = pos
        break
      }
    }
    if (startSeqPos == -1 || startSeqPos == 0) {
      Log.d(TAG, "No valid startpos received from arduino")
      return null
    }
    Log.d(TAG, "Start: $startSeqPos, end: $endSeqPos")

    val jsonBytes = String(buffer, startSeqPos, endSeqPos - startSeqPos + 1)
    Log.d(TAG, "Start: $startSeqPos, end: $endSeqPos, json: $jsonBytes")
    val obj = convertToObject(jsonBytes)

    // Reset the pointer once something is processed
    pointer = 0

    reads++
    lastLine = jsonBytes

    return obj
  }

  fun write(byteArray: ByteArray) {
    Log.d(TAG, "Writing ${byteArray.size} bytes to serial")
    outputSocket.write(byteArray)
    outputSocket.flush()
  }

  private fun convertToObject(jsonBytes: String): JSONObject? {
    try {
      return JSONObject(jsonBytes)
    } catch (ex: Exception) {
      Log.d(TAG, "Error parsing JSON object: $jsonBytes", ex)
      return null
    }
  }

  companion object {
    val startSeq = '{'.code.toByte()
    val endSeq = '}'.code.toByte()
  }
}