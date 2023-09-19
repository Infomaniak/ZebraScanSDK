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

import android.content.Context
import android.content.Intent
import android.os.Bundle

object DataWedgeUtils {

    //region DataWedge Actions
    const val DATAWEDGE_SEND_ACTION = "com.symbol.datawedge.api.ACTION"
    const val DATAWEDGE_ACTION_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION_ACTION"
    const val DATAWEDGE_ACTION_RESULT = "com.symbol.datawedge.api.RESULT_ACTION"
    const val DATAWEDGE_EXTRA_SEND_RESULT = "SEND_RESULT"
    const val DATAWEDGE_EXTRA_RESULT = "RESULT"
    const val DATAWEDGE_EXTRA_COMMAND = "COMMAND"
    const val DATAWEDGE_EXTRA_RESULT_INFO = "RESULT_INFO"
    const val DATAWEDGE_EXTRA_RESULT_CODE = "RESULT_CODE"
    //endregion

    //region Datawedge version
    const val DATAWEDGE_EXTRA_GET_VERSION_INFO = "com.symbol.datawedge.api.GET_VERSION_INFO"
    const val DATAWEDGE_EXTRA_RESULT_GET_VERSION_INFO = "com.symbol.datawedge.api.RESULT_GET_VERSION_INFO"
    const val DATAWEDGE_RETURN_VERSION_DATAWEDGE = "DATAWEDGE"
    //endregion

    //region REGISTER_FOR_NOTIFICATION
    const val DATAWEDGE_EXTRA_REGISTER_NOTIFICATION = "com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION"
    const val DATAWEDGE_EXTRA_UNREGISTER_NOTIFICATION = "com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION"
    const val DATAWEDGE_EXTRA_NOTIFICATION_TYPE = "com.symbol.datawedge.api.NOTIFICATION_TYPE"
    const val DATAWEDGE_EXTRA_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION"
    const val DATAWEDGE_EXTRA_RESULT_NOTIFICATION_TYPE = "NOTIFICATION_TYPE"
    const val DATAWEDGE_EXTRA_SCANNER_STATUS = "SCANNER_STATUS"
    const val DATAWEDGE_EXTRA_PROFILE_SWITCH = "PROFILE_SWITCH"
    const val DATAWEDGE_EXTRA_CONFIGURATION_UPDATE = "CONFIGURATION_UPDATE"
    const val DATAWEDGE_EXTRA_NOTIFICATION_STATUS = "STATUS"
    const val DATAWEDGE_EXTRA_PROFILE_NAME = "PROFILE_NAME"
    //endregion

    const val DATAWEDGE_SCAN_EXTRA_DATA_STRING = "com.symbol.datawedge.data_string"
    const val DATAWEDGE_SCAN_EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type"

    const val DATAWEDGE_SEND_GET_ENUMERATE_SCANNERS = "com.symbol.datawedge.api.ENUMERATE_SCANNERS"
    const val DATAWEDGE_RETURN_ENUMERATE_SCANNERS = "com.symbol.datawedge.api.RESULT_ENUMERATE_SCANNERS"

    const val DATAWEDGE_SEND_GET_CONFIG = "com.symbol.datawedge.api.GET_CONFIG"
    const val DATAWEDGE_RETURN_GET_CONFIG = "com.symbol.datawedge.api.RESULT_GET_CONFIG"
    const val DATAWEDGE_SEND_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG"

    const val DATAWEDGE_SEND_GET_ACTIVE_PROFILE = "com.symbol.datawedge.api.GET_ACTIVE_PROFILE"
    const val DATAWEDGE_RETURN_GET_ACTIVE_PROFILE = "com.symbol.datawedge.api.RESULT_GET_ACTIVE_PROFILE"

    const val DATAWEDGE_SEND_SWITCH_SCANNER = "com.symbol.datawedge.api.SWITCH_SCANNER"

    const val DATAWEDGE_SEND_SET_SCANNER_INPUT = "com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN"
    const val DATAWEDGE_SEND_SET_SCANNER_INPUT_ENABLE = "ENABLE_PLUGIN"
    const val DATAWEDGE_SEND_SET_SCANNER_INPUT_DISABLE = "DISABLE_PLUGIN"

    private const val EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE"
    const val DATAWEDGE_EXTRA_APPLICATION_NAME = "com.symbol.datawedge.api.APPLICATION_NAME"


    //region Soft scan trigger
    private const val EXTRA_SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER"
    private const val EXTRA_SOFT_SCAN_START_SCANNING = "START_SCANNING"
    private const val EXTRA_SOFT_SCAN_STOP_SCANNING = "STOP_SCANNING"
    private const val EXTRA_SOFT_SCAN_TOGGLE_SCANNING = "TOGGLE_SCANNING"
    //endregion

    //region Output intent
    private const val INTENT_DELIVERY_ACTIVITY = "0"
    private const val INTENT_DELIVERY_SERVICE = "1"
    private const val INTENT_DELIVERY_BROADCAST = "2"
    //endregion

    fun createDataWedgeProfile(context: Context, profileName: String, outputIntentAction: String, sendResult: Boolean) {
        //  Create and configure the DataWedge profile associated with this application
        // https://techdocs.zebra.com/datawedge/latest/guide/api/setconfig
        sendCommandString(context, EXTRA_CREATE_PROFILE, profileName, sendResult)

        // Configure created profile to apply to this app
        val profileConfig = Bundle().apply {
            putString("PROFILE_NAME", profileName)
            putString("PROFILE_ENABLED", "true") // These are all strings
            putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")
        }

        // Configure barcode input plugin
        val barcodePlugin = Bundle().apply {
            putString("PLUGIN_NAME", "BARCODE")
            putString("RESET_CONFIG", "true") // This is the default but never hurts to specify
        }
        barcodePlugin.putBundle("PARAM_LIST", Bundle())
        profileConfig.putBundle("PLUGIN_CONFIG", barcodePlugin)

        // Associate profile with this app
        val appConfig = Bundle().apply {
            putString("PACKAGE_NAME", context.packageName)
            putStringArray("ACTIVITY_LIST", arrayOf("*"))
        }
        profileConfig.putParcelableArray("APP_LIST", arrayOf(appConfig))
        sendCommandBundle(context, DATAWEDGE_SEND_SET_CONFIG, profileConfig, sendResult)

        // Configure intent output for captured data to be sent to this app
        val intentPlugin = Bundle().apply {
            putString("PLUGIN_NAME", "INTENT")
            putString("RESET_CONFIG", "true")
        }
        val intentPluginConfig = Bundle().apply {
            putString("intent_output_enabled", "true")
            putString("intent_action", outputIntentAction)
            putString("intent_delivery", INTENT_DELIVERY_BROADCAST)
        }
        intentPlugin.putBundle("PARAM_LIST", intentPluginConfig)
        profileConfig.remove("PLUGIN_CONFIG")
        profileConfig.putBundle("PLUGIN_CONFIG", intentPlugin)
        sendCommandBundle(context, DATAWEDGE_SEND_SET_CONFIG, profileConfig, sendResult)
    }

    fun setConfigForDecoder(
        context: Context, profileName: String, ean8Value: Boolean,
        ean13Value: Boolean, code39Value: Boolean, code128Value: Boolean,
        illuminationValue: String, picklistModeValue: String, sendResult: Boolean
    ) {
        val profileConfig = Bundle().apply {
            putString("PROFILE_NAME", profileName)
            putString("PROFILE_ENABLED", "true")
            putString("CONFIG_MODE", "UPDATE")
        }

        val barcodeConfig = Bundle().apply {
            putString("PLUGIN_NAME", "BARCODE")
            putString("RESET_CONFIG", "true")
        }
        val barcodeParamList = Bundle().apply {
            putString("scanner_selection", "auto")
            putString("decoder_ean8", "$ean8Value")
            putString("decoder_ean13", "$ean13Value")
            putString("decoder_code39", "$code39Value")
            putString("decoder_code128", "$code128Value")
            putString("illumination_mode", illuminationValue)
            putString("picklist", picklistModeValue)
        }
        barcodeConfig.putBundle("PARAM_LIST", barcodeParamList)
        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig)
        sendCommandBundle(context, DATAWEDGE_SEND_SET_CONFIG, profileConfig, sendResult)
    }

    fun toggleSoftScanTrigger(context: Context, sendResult: Boolean) {
        sendCommandString(context, EXTRA_SOFT_SCAN_TRIGGER, EXTRA_SOFT_SCAN_TOGGLE_SCANNING, sendResult)
    }

    fun startSoftScanTrigger(context: Context, sendResult: Boolean) {
        sendCommandString(context, EXTRA_SOFT_SCAN_TRIGGER, EXTRA_SOFT_SCAN_START_SCANNING, sendResult)
    }

    fun stopSoftScanTrigger(context: Context, sendResult: Boolean) {
        sendCommandString(context, EXTRA_SOFT_SCAN_TRIGGER, EXTRA_SOFT_SCAN_STOP_SCANNING, sendResult)
    }

    fun sendCommandString(context: Context, command: String, parameter: String, sendResult: Boolean) {
        val dwIntent = Intent(DATAWEDGE_SEND_ACTION).apply {
            putExtra(command, parameter)
            if (sendResult) putExtra(DATAWEDGE_EXTRA_SEND_RESULT, "true")
        }
        context.sendBroadcast(dwIntent)
    }

    fun sendCommandBundle(context: Context, command: String, parameter: Bundle, sendResult: Boolean) {
        val dwIntent = Intent(DATAWEDGE_SEND_ACTION).apply {
            putExtra(command, parameter)
            if (sendResult) putExtra(DATAWEDGE_EXTRA_SEND_RESULT, "true")
        }
        context.sendBroadcast(dwIntent)
    }
}
