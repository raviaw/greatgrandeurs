//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs.communication

import android.util.Log
import com.raviaw.greatgrandeurs.TAG
import org.json.JSONObject

class BluetoothMessageParser {
  private val lastPartialMessage = ByteArray(1024)
  private var lastPartialMessageSize = 0

  /**
   * Process the byte buffer and return the available messages
   */
  fun processByteBuffer(byteArray: ByteArray, bytesRead: Int): List<BluetoothMessageParser.ParsedMessage> {
    val currentMessage = ByteArray(2048)
    if (lastPartialMessageSize != 0) {
      lastPartialMessage.copyInto(currentMessage, 0, 0, lastPartialMessageSize)
      byteArray.copyInto(currentMessage, lastPartialMessageSize, 0, bytesRead)
    } else {
      byteArray.copyInto(currentMessage, 0, 0, bytesRead)
    }
    val currentSize = lastPartialMessageSize + bytesRead
    lastPartialMessageSize = 0

    val messages = mutableListOf<StringBuilder>()
    var lastMessage: StringBuilder? = null
    var lastStart = 0
    var lastComplete = false
    for (pos in 0 until currentSize) {
      val currentChar = currentMessage[pos].charValue
      if (currentChar == '{') {
        lastComplete = false
        lastMessage = StringBuilder()
        lastMessage.append(currentChar)
        lastStart = pos
      } else if (currentChar == '}') {
        if (lastMessage == null) {
          Log.w(TAG, "Got $currentChar at pos $pos but there was no message")
        } else {
          lastMessage.append(currentChar)
          messages.add(lastMessage)
          lastMessage = null
          lastComplete = true
        }
      } else {
        lastComplete = false
        if (lastMessage == null) {
          Log.w(TAG, "Got $currentChar at pos $pos but there was no message")
        } else {
          lastMessage.append(currentChar)
        }
      }
    }
    if (!lastComplete) {
      Log.d(TAG, "Processing incomplete message")
      currentMessage.copyInto(lastPartialMessage, 0, lastStart, currentSize)
      lastPartialMessageSize = currentSize - lastStart
    }

    val objects = messages.map {
      ParsedMessage(it.toString())
    }

    return objects
  }

  class ParsedMessage(val str: String) {
    val obj by lazy { convertToObject(str) }

    private fun convertToObject(jsonBytes: String): JSONObject? {
      try {
        return JSONObject(jsonBytes)
      } catch (ex: Exception) {
        Log.d(TAG, "Error parsing JSON object: $jsonBytes", ex)
        return null
      }
    }
  }

  private val Byte.charValue
    get() = this.toInt().toChar()
}
