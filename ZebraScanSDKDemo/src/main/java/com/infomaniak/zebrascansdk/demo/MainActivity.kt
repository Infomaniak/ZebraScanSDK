/*
 * Infomaniak ZebraScanSDKDemo - Android
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

package com.infomaniak.zebrascansdk.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infomaniak.zebrascansdk.ZebraScannerCompose
import com.infomaniak.zebrascansdk.demo.ui.theme.InfomaniakZebraScanTheme
import com.infomaniak.zebrascansdk.rememberScannerBinding

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InfomaniakZebraScanTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Main()
                }
            }
        }
    }

    @Composable
    fun Main(defaultScannedData: String = "", defaultScannerStatus: String = "", defaultDataWedgeVersion: String = "") {
        val context = LocalContext.current
        val scannerBinding = rememberScannerBinding(profileName = "InfomaniakZebraScanDemo")
        var scannedData by rememberSaveable { mutableStateOf(defaultScannedData) }
        var scannerStatus by rememberSaveable { mutableStateOf(defaultScannerStatus) }
        var dataWedgeVersion by rememberSaveable { mutableStateOf(defaultDataWedgeVersion) }

        ZebraScannerCompose(
            scannerBinding = scannerBinding,
            onDecodedData = { newScannedData -> scannedData = newScannedData },
            onErrorResulted = { dataWedgeError ->
                Toast.makeText(context, dataWedgeError.name, Toast.LENGTH_SHORT).show()
            },
            onStatusChanged = { scannerStatus = it },
            onReceiveDataWedgeVersion = { dataWedgeVersion = it },
        )

        Column(modifier = Modifier.padding(24.dp)) {
            LabeledText(label = "DataWedge version: ", text = dataWedgeVersion)
            Spacer(modifier = Modifier.height(24.dp))
            LabeledText(label = "Scanner status: ", text = scannerStatus)
            Spacer(modifier = Modifier.height(24.dp))
            LabeledText(label = "Scan data: ", text = scannedData)
            Spacer(modifier = Modifier.height(24.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { scannerBinding.toggleSoftScanTrigger() }) {
                Text(text = "Toggle soft scan")
            }
        }
    }

    @Composable
    private fun LabeledText(label: String, text: String) {
        Row {
            Text(text = label, fontWeight = FontWeight.SemiBold)
            Text(text = text)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainPreview() {
        InfomaniakZebraScanTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Main(defaultScannedData = "data", defaultDataWedgeVersion = "8.2", defaultScannerStatus = "waiting")
            }
        }
    }
}
