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
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.tracking.StarTargets
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCommunication @Inject constructor(
  @ApplicationContext val context: Context,
  val applicationState: ApplicationState,
  val arduinoJsonProcessor: ArduinoJsonProcessor
) : IBluetoothImplementation {
  //

  private val bluetoothManager: BluetoothManager? by lazy { context.getSystemService(BluetoothManager::class.java) }
  private val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager?.adapter }

  override val adapterAvailable: Boolean
    get() = bluetoothAdapter != null
  override val connected: Boolean
    get() = applicationState.bluetoothState.bluetoothConnection != null
  var lastCommunicationTime: Long = -1

  override val arduinoSlaveMode: Boolean
    get() = this.localArduinoSlaveMode

  private var localArduinoSlaveMode = false

  override val arduinoLightsOn: Boolean
    get() = this.localArduinoLightsOn

  private var localArduinoLightsOn = true

  //
  // Thread responsible for reading the arduino data
  private val readThread = ReadThread().apply { start() }

  override fun devices(): Set<FoundBluetoothDevice> = devicesInternal().map { FoundBluetoothDevice.fromDevice(it) }.toSet()

  @SuppressLint("MissingPermission")
  override fun connectToDevice(device: FoundBluetoothDevice) {
    Log.d(TAG, "Connecting to device: $device")
    val socket = device.nnNativeDevice.createRfcommSocketToServiceRecord(uuid)
    socket.connect()

    applicationState.bluetoothState.selectedDevice = device
    applicationState.bluetoothState.bluetoothConnection = BluetoothConnection(arduinoJsonProcessor = arduinoJsonProcessor, device = device, socket = socket)
  }

  // region Commands
  //
  override fun sendMenuMain() {
    Log.d(TAG, "Sending menu main")
    ArduinoCommand.SendMenuMain.send(this)
  }

  override fun sendTime() {
    Log.d(TAG, "Sending current time")
    ArduinoCommand.Time.send(this)
  }

  override fun sendArduinoSlaveMode() {
    Log.d(TAG, "Sending slave mode")
    ArduinoCommand.SlaveMode.send(this)
    this.localArduinoSlaveMode = true
  }

  override fun sendArduinoFreeMode() {
    Log.d(TAG, "Sending mater mode")
    ArduinoCommand.MasterMode.send(this)
    this.localArduinoSlaveMode = false
  }

  override fun sendLightsOn() {
    Log.d(TAG, "Sending lights on")
    ArduinoCommand.LightsOn.send(this)
    this.localArduinoLightsOn = true
  }

  override fun sendLightsOff() {
    Log.d(TAG, "Sending lights off")
    ArduinoCommand.LightsOff.send(this)
    this.localArduinoLightsOn = false
  }

  override fun sendStartCalibrating(index: Int, starTarget: StarTargets.Target) {
    Log.d(TAG, "Sending start calibrating ($index, target: $starTarget)")
    ArduinoCommand.StartCalibrating(index, starTarget).send(this)
  }

  override fun sendCalibratingMoveSpeed(x: Int, y: Int, speed: Float) {
    Log.d(TAG, "Sending move speed ($x, $y)")
    ArduinoCommand.CalibratingMoveSpeed(x, y, speed.toInt()).send(this)
  }

  override fun sendCalibratingMoveStop() {
    Log.d(TAG, "Sending move stop")
    ArduinoCommand.CalibratingMoveStop.send(this)
  }

  override fun sendStoreCalibration() {
    Log.d(TAG, "Sending store calibration")
    ArduinoCommand.StoreCalibration.send(this)
  }

  override fun sendCalibrationCompleted() {
    Log.d(TAG, "Sending calibration completed")
    ArduinoCommand.CalibrationCompleted.send(this)
  }

  override fun sendFindStar(index: Int, starTarget: StarTargets.Target) {
    Log.d(TAG, "Sending find star ($index, target: $starTarget)")
    ArduinoCommand.FindStar(index, starTarget).send(this)
  }
//
  // endregion

  fun writeToCurrentDevice(bytes: ByteArray) {
    if (applicationState.bluetoothState.bluetoothConnection == null) {
      Log.w(TAG, "There is no connection");
      return;
    }

    // Writes
    Log.d(TAG, "Writing bytes to serial");
    applicationState.bluetoothState.bluetoothConnection?.write(bytes)
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
        applicationState.bluetoothState.bluetoothConnection?.let {
          val objects = it.readNextObjects()
          if (objects.isNotEmpty()) {
            lastCommunicationTime = System.currentTimeMillis()
            for (obj in objects) {
              arduinoJsonProcessor.processJson(obj)
            }
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
