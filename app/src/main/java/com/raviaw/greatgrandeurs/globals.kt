//
// Copyright 2024 Ravi Ambros Wallau - raviaw.com.
// All Rights Reserved.
//
// $Id$
//
package com.raviaw.greatgrandeurs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

const val TAG = "com.raviaw.greatgrandeurs"
val StandardPadding = 6.dp

//
fun Modifier.standardPadding() = this.padding(StandardPadding)

//
fun Modifier.standardHorizontalPadding() = this.padding(horizontal = StandardPadding)

//
fun Modifier.standardVerticalPadding() = this.padding(vertical = StandardPadding)

@Composable
fun HorizontalSpacer() = Spacer(modifier = Modifier.width(StandardPadding))

@Composable
fun VerticalSpacer() = Spacer(modifier = Modifier.height(StandardPadding))