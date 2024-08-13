//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.state

import com.raviaw.greatgrandeurs.communication.BluetoothConnection
import com.raviaw.greatgrandeurs.communication.FoundBluetoothDevice

class BluetoothState {
  var selectedDevice: FoundBluetoothDevice? = null
  var bluetoothConnection: BluetoothConnection? = null
}
