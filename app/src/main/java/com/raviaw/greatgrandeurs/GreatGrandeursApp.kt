//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raviaw.greatgrandeurs.communication.BluetoothCommunication
import com.raviaw.greatgrandeurs.compose.BluetoothScreen
import com.raviaw.greatgrandeurs.compose.CalibrateScreen
import com.raviaw.greatgrandeurs.compose.HomeScreen
import com.raviaw.greatgrandeurs.compose.MoveScreen
import com.raviaw.greatgrandeurs.state.ApplicationState
import com.raviaw.greatgrandeurs.tracking.StarTargets

@Composable
fun GreatGrandeursApp(bluetoothCommunication: BluetoothCommunication, applicationState: ApplicationState, starTargets: StarTargets) {
  val navController = rememberNavController()
  GreatGrandeursNavHost(
    navController = navController,
    bluetoothCommunication = bluetoothCommunication,
    applicationState = applicationState,
    starTargets = starTargets
  )
}

@Composable
fun GreatGrandeursNavHost(
  navController: NavHostController,
  bluetoothCommunication: BluetoothCommunication,
  applicationState: ApplicationState,
  starTargets: StarTargets
) {
  Log.d(TAG, "Initializing app")
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        applicationState = applicationState,
        onCalibrate = {
          navController.navigate(
            Screen.Calibrate.createRoute()
          )
        },
        onMove = {
          navController.navigate(
            Screen.Move.createRoute()
          )
        },
        onFind = {
          navController.navigate(
            Screen.Find.createRoute()
          )
        },
        onReport = {
          navController.navigate(
            Screen.Report.createRoute()
          )
        },
        onBluetooth = {
          navController.navigate(
            Screen.Bluetooth.createRoute()
          )
        },
        onSettings = {
          navController.navigate(
            Screen.Settings.createRoute()
          )
        }
      )
    }
    composable(
      route = Screen.Move.route,
      arguments = Screen.Move.navArguments
    ) {
      MoveScreen(
        onBackClick = { navController.navigateUp() }
      )
    }
    composable(
      route = Screen.Calibrate.route,
      arguments = Screen.Calibrate.navArguments
    ) {
      CalibrateScreen(
        starTargets = starTargets,
        onBackClick = { navController.navigateUp() }
      )
    }
    composable(
      route = Screen.Bluetooth.route,
      arguments = Screen.Bluetooth.navArguments
    ) {
      BluetoothScreen(
        bluetoothCommunication = bluetoothCommunication,
        applicationState = applicationState,
        onBackClick = { navController.navigateUp() }
      )
    }
//    composable(
//      route = Screen.Gallery.route,
//      arguments = Screen.Gallery.navArguments
//    ) {
//      GalleryScreen(
//        onPhotoClick = {
//          val uri = Uri.parse(it.user.attributionUrl)
//          val intent = Intent(Intent.ACTION_VIEW, uri)
//          activity.startActivity(intent)
//        },
//        onUpClick = {
//          navController.navigateUp()
//        })
//    }
  }
}
