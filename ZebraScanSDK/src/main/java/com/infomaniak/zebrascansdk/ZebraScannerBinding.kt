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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_ACTION_RESULT
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_ACTION_RESULT_NOTIFICATION
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_APPLICATION_NAME
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_COMMAND
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_GET_VERSION_INFO
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_NOTIFICATION_TYPE
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_REGISTER_NOTIFICATION
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT_CODE
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT_GET_VERSION_INFO
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT_INFO
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT_NOTIFICATION
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_RESULT_NOTIFICATION_TYPE
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_SCANNER_STATUS
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_EXTRA_UNREGISTER_NOTIFICATION
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_RETURN_VERSION_DATAWEDGE
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_SCAN_EXTRA_DATA_STRING
import com.infomaniak.zebrascansdk.DataWedgeUtils.DATAWEDGE_SCAN_EXTRA_LABEL_TYPE


class ZebraScannerBinding(
    private val context: Context,
    private val profileName: String,
    private val outputIntentAction: String = "${context.packageName}.SCAN",
) : DefaultLifecycleObserver {

    private val dataWedgeBroadcast = DataWedgeBroadcast()

    var onDecodedData: ((data: String) -> Unit)? = null
    var onErrorResulted: ((dataWedgeError: DataWedgeError) -> Unit)? = null
    var onReceiveDataWedgeVersion: ((dataWedgeVersion: String) -> Unit)? = null
    var onStatusChanged: ((scannerStatus: String) -> Unit)? = null

    private inline val sendResult get() = onErrorResulted != null

    fun bind(lifecycle: Lifecycle) {
        Log.i(TAG, "bind()")
        lifecycle.addObserver(this)
        DataWedgeUtils.createDataWedgeProfile(context, profileName, outputIntentAction, sendResult)
    }

    fun unbind() {
        Log.i(TAG, "unbind()")
        unregisterForScannerDataResult()
        unRegisterScannerStatus()
    }

    fun toggleSoftScanTrigger() {
        Log.i(TAG, "toggleSoftScanTrigger()")
        DataWedgeUtils.toggleSoftScanTrigger(context, sendResult)
    }

    fun startSoftScanTrigger() {
        Log.i(TAG, "startSoftScanTrigger()")
        DataWedgeUtils.startSoftScanTrigger(context, sendResult)
    }

    fun stopSoftScanTrigger() {
        Log.i(TAG, "stopSoftScanTrigger()")
        DataWedgeUtils.stopSoftScanTrigger(context, sendResult)
    }

    override fun onResume(owner: LifecycleOwner) {
        registerForScannerDataResult()
        registerForScannerStatus()
        DataWedgeUtils.sendCommandString(context, DATAWEDGE_EXTRA_GET_VERSION_INFO, "", sendResult)
    }

    override fun onPause(owner: LifecycleOwner) {
        unbind()
    }

    private fun registerForScannerDataResult() {
        Log.i(TAG, "registerForScannerDataResult()")
        val intent = IntentFilter(outputIntentAction).apply {
            addAction(DATAWEDGE_ACTION_RESULT)
            addAction(DATAWEDGE_ACTION_RESULT_NOTIFICATION)
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        ContextCompat.registerReceiver(context, dataWedgeBroadcast, intent, ContextCompat.RECEIVER_EXPORTED)
    }

    private fun unregisterForScannerDataResult() {
        Log.i(TAG, "unregisterForScannerDataResult()")
        context.unregisterReceiver(dataWedgeBroadcast)
    }

    private fun registerForScannerStatus() {
        Log.i(TAG, "registerForScannerStatus()")
        // Use REGISTER_FOR_NOTIFICATION: http://techdocs.zebra.com/datawedge/latest/guide/api/registerfornotification/
        val scannerStatusBundle = Bundle().apply {
            putString(DATAWEDGE_EXTRA_APPLICATION_NAME, context.packageName)
            putString(DATAWEDGE_EXTRA_NOTIFICATION_TYPE, DATAWEDGE_EXTRA_SCANNER_STATUS)
        }
        DataWedgeUtils.sendCommandBundle(context, DATAWEDGE_EXTRA_REGISTER_NOTIFICATION, scannerStatusBundle, sendResult)
    }

    private fun unRegisterScannerStatus() {
        Log.i(TAG, "unRegisterScannerStatus()")
        val scannerStatusBundle = Bundle().apply {
            putString(DATAWEDGE_EXTRA_APPLICATION_NAME, context.packageName)
            putString(DATAWEDGE_EXTRA_NOTIFICATION_TYPE, DATAWEDGE_EXTRA_SCANNER_STATUS)
        }
        DataWedgeUtils.sendCommandBundle(context, DATAWEDGE_EXTRA_UNREGISTER_NOTIFICATION, scannerStatusBundle, sendResult)
    }

    inner class DataWedgeBroadcast : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // Get DataWedge version info
            intent.handleDataWedgeVersion()

            // Get DataWedge result
            when (intent.action) {
                outputIntentAction -> intent.handleResultedData()
                DATAWEDGE_ACTION_RESULT -> intent.handleResultedError()
                DATAWEDGE_ACTION_RESULT_NOTIFICATION -> handleNotificationResult(intent)
            }
        }

        private fun Intent.handleDataWedgeVersion() {
            if (hasExtra(DATAWEDGE_EXTRA_RESULT_GET_VERSION_INFO)) {
                val versionInfo = getBundleExtra(DATAWEDGE_EXTRA_RESULT_GET_VERSION_INFO)
                versionInfo?.getString(DATAWEDGE_RETURN_VERSION_DATAWEDGE)?.let { version ->
                    onReceiveDataWedgeVersion?.invoke(version)
                    Log.i(TAG, "DataWedge Version:$version is ready")
                }
            }
        }

        private fun Intent.handleResultedData() {
            val decodedData = getStringExtra(DATAWEDGE_SCAN_EXTRA_DATA_STRING)
            val decodedLabelType = getStringExtra(DATAWEDGE_SCAN_EXTRA_LABEL_TYPE)
            decodedData?.let { onDecodedData?.invoke(it) }
            Log.d(TAG, "Data resulted. data:$decodedData \tdecodedLabelType:$decodedLabelType")
        }

        private fun Intent.handleResultedError() {
            if (hasExtra(DATAWEDGE_EXTRA_RESULT) && hasExtra(DATAWEDGE_EXTRA_COMMAND)) {
                val result = getStringExtra(DATAWEDGE_EXTRA_RESULT)
                val command = getStringExtra(DATAWEDGE_EXTRA_COMMAND)
                val errorCode = getBundleExtra(DATAWEDGE_EXTRA_RESULT_INFO)?.getString(DATAWEDGE_EXTRA_RESULT_CODE)

                errorCode?.let {
                    onErrorResulted?.invoke(DataWedgeError.parse(it))
                    Log.e(TAG, "$result Resulted. Command:$command, errorCode:$errorCode")
                }
            }
        }

        private fun handleNotificationResult(intent: Intent) {
            if (intent.hasExtra(DATAWEDGE_EXTRA_RESULT_NOTIFICATION)) {
                val extras = intent.getBundleExtra(DATAWEDGE_EXTRA_RESULT_NOTIFICATION)

                when (extras?.getString(DATAWEDGE_EXTRA_RESULT_NOTIFICATION_TYPE)) {
                    DATAWEDGE_EXTRA_SCANNER_STATUS -> {
                        val scannerStatus = extras.getString(DataWedgeUtils.DATAWEDGE_EXTRA_NOTIFICATION_STATUS)
                        val profileName = extras.getString(DataWedgeUtils.DATAWEDGE_EXTRA_PROFILE_NAME)
                        val displayScannerStatusText = "$scannerStatus, profile: $profileName"
                        scannerStatus?.let { onStatusChanged?.invoke(it) }
                        Log.i(TAG, "Scanner status: $displayScannerStatusText")
                    }
                    // Here: we can handle others notifications status
                }
            }
        }
    }

    private companion object {
        val TAG: String = ZebraScannerBinding::class.java.simpleName
    }
}
