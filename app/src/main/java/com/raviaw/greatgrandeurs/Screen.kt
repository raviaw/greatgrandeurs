/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raviaw.greatgrandeurs

import androidx.navigation.NamedNavArgument

sealed class Screen(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  data object Home : Screen("home")

  data object Move : Screen(route = "move") {
    fun createRoute() = "move"
  }

  data object Calibrate : Screen(route = "calibrate") {
    fun createRoute() = "calibrate"
  }

  data object Find : Screen(route = "find") {
    fun createRoute() = "find"
  }

  data object Report : Screen(route = "report") {
    fun createRoute() = "report"
  }

  data object Bluetooth : Screen(route = "bluetooth") {
    fun createRoute() = "bluetooth"
  }

  data object Settings : Screen(route = "settings") {
    fun createRoute() = "settings"
  }

  data object MoveControls : Screen(route = "moveControls") {
    fun createRoute() = "moveControls"
  }
}
