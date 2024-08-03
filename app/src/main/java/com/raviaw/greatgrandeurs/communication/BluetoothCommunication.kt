//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCommunication(@Inject val context: Context) {
  private val bluetoothManager: BluetoothManager? = context.getSystemService(BluetoothManager::class.java)
  private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

  init {
    Log.d(TAG, "Bluetooth manager: $bluetoothManager")
    Log.d(TAG, "Bluetooth adapter: $bluetoothAdapter")
  }
}