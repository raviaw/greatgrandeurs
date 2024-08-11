//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

class FoundBluetoothDevice(val name: String, val address: String, val nativeDevice: BluetoothDevice?) {
  val nnNativeDevice = requireNotNull(nativeDevice) { "No native device was found" }

  companion object {
    @SuppressLint("MissingPermission")
    fun fromDevice(bluetoothDevice: BluetoothDevice) =
      FoundBluetoothDevice(
        name = bluetoothDevice.name,
        address = bluetoothDevice.address,
        nativeDevice = bluetoothDevice
      )
  }

  override fun toString(): String {
    return "FoundBluetoothDevice(name='$name', address='$address', nativeDevice=$nativeDevice)"
  }
}