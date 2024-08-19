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

class BluetoothConnection(val arduinoJsonProcessor: ArduinoJsonProcessor, val device: FoundBluetoothDevice, val socket: BluetoothSocket) {
  private val bluetoothMessageParser = BluetoothMessageParser()
  private val buffer = ByteArray(1024)
  private val inputSocket = socket.inputStream
  private val outputSocket = BufferedOutputStream(socket.outputStream)

  var reads: Long = 0
  var lastLine: String? = null
  val name = device.name

  fun readNextObjects(): List<JSONObject> {
    Log.d(TAG, "Reading next lines from device: $device")
    val readNow = inputSocket.read(buffer)
    if (readNow == 0) {
      Log.d(TAG, "No input from arduino")
      return emptyList()
    }

    val messages = bluetoothMessageParser.processByteBuffer(buffer, readNow)
    if (messages.isNotEmpty()) {
      val lastMessage = messages.last()
      lastLine = lastMessage.str
    }
    reads += messages.size

    return messages.mapNotNull { it.obj }
  }

  fun write(byteArray: ByteArray) {
    Log.d(TAG, "Writing ${byteArray.size} bytes to serial")
    outputSocket.write(byteArray)
    outputSocket.flush()
  }

  companion object {
    val startSeq = '{'.code.toByte()
    val endSeq = '}'.code.toByte()
  }
}
