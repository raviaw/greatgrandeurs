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
}