//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raviaw.greatgrandeurs.compose.HomeScreen
import com.raviaw.greatgrandeurs.compose.MoveScreen

@Composable
fun GreatGrandeursApp() {
  val navController = rememberNavController()
  GreatGrandeursNavHost(
    navController = navController
  )
}

@Composable
fun GreatGrandeursNavHost(
  navController: NavHostController
) {
  val activity = (LocalContext.current as Activity)
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
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
