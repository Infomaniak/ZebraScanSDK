/*
 * Infomaniak ZebraScanSDK - Android
 * Copyright (C) 2023 Infomaniak Network SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
