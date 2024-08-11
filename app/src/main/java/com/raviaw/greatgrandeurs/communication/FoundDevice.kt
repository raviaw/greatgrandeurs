//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

class FoundBluetoothDevice(val name: String, val address: String) {
  companion object {
    @SuppressLint("MissingPermission")
    fun fromDevice(bluetoothDevice: BluetoothDevice) = FoundBluetoothDevice(name = bluetoothDevice.name, address = bluetoothDevice.address)
  }
}