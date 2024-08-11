//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.bluetooth.BluetoothDevice

interface IBluetoothImplementation {
  val adapterAvailable:Boolean
  fun devices(): Set<FoundBluetoothDevice>?
}