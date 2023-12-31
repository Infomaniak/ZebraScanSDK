@file:Suppress("SpellCheckingInspection")

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

buildscript {
    extra.apply {
        set("javaVersion", JavaVersion.VERSION_17)
        set("minSdkExtra", 24)
        set("targetSdkExtra", 34)
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.1" apply false
}
