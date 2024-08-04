//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.raviaw.greatgrandeurs.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCommunication @Inject constructor(@ApplicationContext val context: Context) {
  val bluetoothManager: BluetoothManager? = context.getSystemService(BluetoothManager::class.java)
  val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

  fun devices(): Set<BluetoothDevice>? {
    Log.d(TAG, "Bluetooth adapter: $bluetoothAdapter")

    val bluetoothAdapter = bluetoothAdapter
    if (bluetoothAdapter == null) {
      return null
    }

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
      Log.d(TAG, "Bluetooth is not accessible")
      return null
    }

    val devices = bluetoothAdapter.bondedDevices
    Log.d(TAG, "Bluetooth is not accessible: $devices")
    return devices
  }

  @SuppressLint("MissingPermission")
  fun connectNamedDevice(name: String = "HC-06"): BluetoothDevice? {
    val devices = devices().orEmpty()
    val device = devices.find { it.name == name }
    return device
  }

  init {
    Log.d(TAG, "Bluetooth manager: $bluetoothManager")
    Log.d(TAG, "Bluetooth adapter: $bluetoothAdapter")
  }
}