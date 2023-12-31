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

enum class DataWedgeError {
    /** An attempt was made to associate an app that was already associated with another Profile */
    APP_ALREADY_ASSOCIATED,

    /** The bundle contains no data */
    BUNDLE_EMPTY,

    /** An attempt was made to disable DataWedge when it was already disabled */
    DATAWEDGE_ALREADY_DISABLED,

    /** An attempt was made to enable DataWedge when it was already enabled */
    DATAWEDGE_ALREADY_ENABLED,

    /** An attempt was made to perform an operation when DataWedge was disabled */
    DATAWEDGE_DISABLED,

    /** An attempt was made to acquire data when the Barcode or SimulScan plug-in was disabled */
    INPUT_NOT_ENABLED,

    /** An attempt was made to rename or delete a protected Profile or to associate an app with Profile0 */
    OPERATION_NOT_ALLOWED,

    /** The passed parameters were empty, null or invalid */
    PARAMETER_INVALID,

    /** A passed plug-in parameter bundle is empty or contains insufficient information */
    PLUGIN_BUNDLE_INVALID,

    /** An attempt was made to enable or disable the scanner when barcode plug-in was disabled manually from the DataWedge UI */
    PLUGIN_DISABLED_IN_CONFIG,

    /** An attempt was made to configure a plug-in that is not supported by DataWedge intent APIs */
    PLUGIN_NOT_SUPPORTED,

    /** An attempt was made to create, clone or rename a Profile with a name that already exists */
    PROFILE_ALREADY_EXISTS,

    /** An attempt was made to set the default Profile as the default Profile */
    PROFILE_ALREADY_SET,

    /** An attempt was made to perform an operation on a disabled Profile */
    PROFILE_DISABLED,

    /** An attempt was made to switch to or set as the default a Profile that is already associated with another app */
    PROFILE_HAS_APP_ASSOCIATION,

    /** An attempt was made to configure an empty Profile name */
    PROFILE_NAME_EMPTY,

    /** An attempt was made to perform an operation on a Profile that does not exist */
    PROFILE_NOT_FOUND,

    /** An attempt was made to disable a scanner that is already disabled */
    SCANNER_ALREADY_DISABLED,

    /** An attempt was made to enable a scanner that is already enabled */
    SCANNER_ALREADY_ENABLED,

    /** An exception occurred while disabling the scanner */
    SCANNER_DISABLE_FAILED,

    /** An exception occurred while enabling the scanner */
    SCANNER_ENABLE_FAILED,

    /** An attempt was made to call SetConfig or Switch Scanner Params API to change the scanning mode to MultiBarcode or NextGen SimulScan on an unlicensed */
    UNLICENSED_FEATURE,

    /** An unidentified error occurred */
    UNKNOWN;

    companion object {
        fun parse(errorCode: String) = runCatching {
            DataWedgeError.valueOf(errorCode)
        }.getOrDefault(UNKNOWN)
    }
}
