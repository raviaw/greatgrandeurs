//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs

import android.util.Log
import com.raviaw.greatgrandeurs.communication.BluetoothMessageParser
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test

class TestBluetoothParsing {
  @Test
  fun test() {
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.d(any(), any()) } returns 0
    every { Log.w(any(), any(String::class)) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0

    val bluetoothMessageParser = BluetoothMessageParser()
    val run0 = bluetoothMessageParser.appendString("{a=1")
    val run1 = bluetoothMessageParser.appendString("}{b=1")
    val run2 = bluetoothMessageParser.appendString("}{c=1}{d=2}")
    Assert.assertEquals(0, run0.size)
    Assert.assertEquals(1, run1.size)
    Assert.assertEquals(3, run2.size)
    Assert.assertEquals("{a=1}", run1[0].str)
    Assert.assertEquals("{b=1}", run2[0].str)
    Assert.assertEquals("{c=1}", run2[1].str)
    Assert.assertEquals("{d=2}", run2[2].str)
  }

  @Test
  fun testCrap() {
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.d(any(), any()) } returns 0
    every { Log.w(any(), any(String::class)) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0

    val bluetoothMessageParser = BluetoothMessageParser()
    val run0 = bluetoothMessageParser.appendString("asdasd23123{a=1")
    val run1 = bluetoothMessageParser.appendString("}asdadsdw12121{b=1")
    val run2 = bluetoothMessageParser.appendString("}{c=1}{d=2}")
    Assert.assertEquals(0, run0.size)
    Assert.assertEquals(1, run1.size)
    Assert.assertEquals(3, run2.size)
    Assert.assertEquals("{a=1}", run1[0].str)
    Assert.assertEquals("{b=1}", run2[0].str)
    Assert.assertEquals("{c=1}", run2[1].str)
    Assert.assertEquals("{d=2}", run2[2].str)
  }

  private fun BluetoothMessageParser.appendString(str: String): List<BluetoothMessageParser.ParsedMessage> =
    this.processByteBuffer(str.toByteArray(Charsets.UTF_8), str.length)
}
