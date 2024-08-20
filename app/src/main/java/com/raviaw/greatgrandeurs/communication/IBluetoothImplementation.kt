//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

interface IBluetoothImplementation : ArduinoCommander {
  val adapterAvailable: Boolean

  fun devices(): Set<FoundBluetoothDevice>?
  fun connectToDevice(device: FoundBluetoothDevice)

  companion object {
    val EMPTY = object : IBluetoothImplementation {
      override val adapterAvailable: Boolean = false
      override val connected: Boolean = false
      override val arduinoSlaveMode: Boolean = false
      override val arduinoLightsOn: Boolean = true

      override fun devices(): Set<FoundBluetoothDevice> = emptySet()

      override fun connectToDevice(device: FoundBluetoothDevice) {
        throw UnsupportedOperationException()
      }
    }
  }
}
