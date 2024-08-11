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

class BluetoothConnection(val device: FoundBluetoothDevice, val socket: BluetoothSocket) {
  val buffer = ByteArray(1024)
  val inputSocket = socket.inputStream
  var reads: Int = 0
  var lastLine: String? = null

  val name = device.name

  fun readNextLines() {
    Log.d(TAG, "Reading next lines from device: $device")
    val bytes = inputSocket.read(buffer)
    if (bytes == 0) {
      Log.d(TAG, "Last data read was empty")
      return
    }
    Log.d(TAG, "Read valid data from device")
    reads++
    val string = String(buffer)
    Log.d(TAG, "Last string received: $string")
    val lines = String(buffer).split("\n")
    reads += lines.size
    lastLine = lines.lastOrNull()
  }
}