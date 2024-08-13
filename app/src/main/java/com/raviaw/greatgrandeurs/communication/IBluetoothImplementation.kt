//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

interface IBluetoothImplementation {
  val adapterAvailable: Boolean
  var selectedDevice: FoundBluetoothDevice?
  var bluetoothConnection: BluetoothConnection?

  fun devices(): Set<FoundBluetoothDevice>?
  fun connectToDevice(device: FoundBluetoothDevice)
  fun sendTime()

  companion object {
    val EMPTY = object : IBluetoothImplementation {
      override val adapterAvailable: Boolean = false
      override var selectedDevice: FoundBluetoothDevice? = null
      override var bluetoothConnection: BluetoothConnection? = null

      override fun devices(): Set<FoundBluetoothDevice> = emptySet()

      override fun connectToDevice(device: FoundBluetoothDevice) {
        throw UnsupportedOperationException()
      }

      override fun sendTime() {
        throw UnsupportedOperationException()
      }
    }
  }
}