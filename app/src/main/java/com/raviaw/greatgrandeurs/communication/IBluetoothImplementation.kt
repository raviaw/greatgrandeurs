//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

interface IBluetoothImplementation : ArduinoCommander {
  val adapterAvailable: Boolean

  fun devices(): Set<FoundBluetoothDevice>? = emptySet()
  fun connectToDevice(device: FoundBluetoothDevice) {}
  fun disconnectFromCurrentDevice() {}

  companion object {
    val EMPTY = object : IBluetoothImplementation {
      override val adapterAvailable: Boolean = false
      override val connected: Boolean = false
      override val arduinoSlaveMode: Boolean = false
      override val arduinoLightsOn: Boolean = true
      override val laserOn: Boolean = false

      override fun devices(): Set<FoundBluetoothDevice> = emptySet()
    }
  }
}
