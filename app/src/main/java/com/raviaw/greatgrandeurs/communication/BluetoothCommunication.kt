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
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCommunication @Inject constructor(
  @ApplicationContext val context: Context,
  val arduinoJsonProcessor: ArduinoJsonProcessor
) : IBluetoothImplementation {
  //

  val bluetoothManager: BluetoothManager? by lazy { context.getSystemService(BluetoothManager::class.java) }
  val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager?.adapter }

  override var selectedDevice: FoundBluetoothDevice? = null
  override var bluetoothConnection: BluetoothConnection? = null

  override val adapterAvailable: Boolean
    get() = bluetoothAdapter != null

  var lastCommunicationTime: Long = -1

  //
  // Thread responsible for reading the arduino data
  private val readThread = ReadThread().apply { start() }

  override fun devices(): Set<FoundBluetoothDevice> = devicesInternal().map { FoundBluetoothDevice.fromDevice(it) }.toSet()

  @SuppressLint("MissingPermission")
  override fun connectToDevice(device: FoundBluetoothDevice) {
    Log.d(TAG, "Connecting to device: $device")
    val socket = device.nnNativeDevice.createRfcommSocketToServiceRecord(uuid)
    socket.connect()

    selectedDevice = device
    bluetoothConnection = BluetoothConnection(arduinoJsonProcessor = arduinoJsonProcessor, device = device, socket = socket)
  }

  override fun sendTime() {
    Log.d(TAG, "Sending current time")
    ArduinoCommand.SendTime.send(this)
  }

  fun writeToCurrentDevice(bytes: ByteArray) {
    if (bluetoothConnection == null) {
      Log.w(TAG, "There is no connection");
      return;
    }

    // Writes
    Log.d(TAG, "Writing bytes to serial");
    bluetoothConnection?.write(bytes)
  }

  private fun devicesInternal(): Set<BluetoothDevice> {
    Log.d(TAG, "Bluetooth adapter: $bluetoothAdapter")

    val bluetoothAdapter = bluetoothAdapter
    if (bluetoothAdapter == null) {
      Log.d(TAG, "Bluetooth is not accessible - adapter is null")
      return emptySet()
    }

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
      Log.d(TAG, "Bluetooth is not accessible - no permissions")
      return emptySet()
    }

    val devices = bluetoothAdapter.bondedDevices
    Log.d(TAG, "Bluetooth is not accessible: $devices is null")

    return devices.orEmpty()
  }

//  @SuppressLint("MissingPermission")
//  fun connectNamedDevice(name: String = "HC-06"): BluetoothDevice? {
//    val devices = devices().orEmpty()
//    val device = devices.find { it.name == name }
//    return device
//  }

//  @SuppressLint("MissingPermission")
//  fun testConnection(bluetoothDevice: BluetoothDevice) {
//  }

  private inner class ReadThread : Thread("arduino-read-thread") {
    override fun run() {
      while (true) {
        bluetoothConnection?.let {
          val obj = it.readNextObject()
          if (obj != null) {
            lastCommunicationTime = System.currentTimeMillis()
            arduinoJsonProcessor.processJson(obj)
          }
        }
        sleep(250);
      }
    }
  }

  init {
    Log.d(TAG, "Bluetooth manager: $bluetoothManager")
    Log.d(TAG, "Bluetooth adapter: $bluetoothAdapter")
  }

  companion object {
    val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
  }
}