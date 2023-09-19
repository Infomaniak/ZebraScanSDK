/*
 * Infomaniak ZebraScanSDK - Android
 * Copyright (C) 2023 Infomaniak Network SA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.infomaniak.zebrascansdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun ZebraScannerCompose(
    scannerBinding: ZebraScannerBinding,
    onDecodedData: (data: String) -> Unit,
    onErrorResulted: (dataWedgeError: DataWedgeError) -> Unit,
    onStatusChanged: ((scannerStatus: String) -> Unit)? = null,
    onReceiveDataWedgeVersion: ((dataWedgeVersion: String) -> Unit)? = null,
) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    LaunchedEffect(lifecycleOwner) {
        scannerBinding.bind(lifecycleOwner.lifecycle)
    }

    LaunchedEffect(Unit) {
        scannerBinding.onDecodedData = { data ->
            onDecodedData(data)
        }

        scannerBinding.onErrorResulted = { error ->
            onErrorResulted(error)
        }

        scannerBinding.onStatusChanged = { scannerStatus ->
            onStatusChanged?.invoke(scannerStatus)
        }

        scannerBinding.onReceiveDataWedgeVersion = { dataWedgeVersion ->
            onReceiveDataWedgeVersion?.invoke(dataWedgeVersion)
        }
    }
}

@Composable
fun rememberScannerBinding(profileName: String): ZebraScannerBinding {
    val context = LocalContext.current
    return remember {
        ZebraScannerBinding(context, profileName)
    }
}
