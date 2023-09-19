# ZebraScanSDK Documentation

ZebraScanSDK is a library developed by Infomaniak Network SA for Android applications to easily integrate and manage Zebra barcode scanners using DataWedge.
This documentation provides an overview of the library's features, installation instructions, and usage examples.

## Installation

To use ZebraScanSDK in your Android project, follow these installation steps:

### Gradle

Add the following dependency to your project's `build.gradle` file:

```gradle
implementation 'com.infomaniak.zebrascansdk:zebrascansdk:1.0.0'
```

Sync your project with Gradle files to make sure the library is downloaded.

## Usage

### Using ZebraScanSDK in Compose

```kotlin
@Composable
fun MyComposable() {
    val scannerBinding = rememberScannerBinding(profileName = "MyScannerProfile")

    ZebraScannerCompose(
        scannerBinding = scannerBinding,
        onDecodedData = { data ->
            // Handle the decoded data
        },
        onErrorResulted = { error ->
            // Handle scanner errors
        },
        onStatusChanged = { status ->
            // Handle scanner status
        },
        onReceiveDataWedgeVersion = { dataWedgeVersion ->
            // Handle DataWedge version
        },
    )
}
```

### Using ZebraScanSDK without Compose

```kotlin
class MyActivity : AppCompatActivity() {
    private val zebraScannerBinding: ZebraScannerBinding by lazy {
        ZebraScannerBinding(this, "MyScannerProfile")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        zebraScannerBinding.bind(lifecycle = this)

        // Implement your scanner data and error handling logic here
    }
}
```

## Example Project

To see ZebraScanSDK in action, you can explore the [ZebraScanSDKDemo](https://github.com/Infomaniak/ZebraScanSDK/ZebraScanSDKDemo) project available in the same repository. 
This open-source project provides a comprehensive example of how to integrate and use the library in a real-world Android application.

## Contributions

We welcome contributions from the developer community. If you would like to contribute to ZebraScanSDK, please do so by creating a pull request.
We appreciate your input in improving the library and making it more user-friendly.

## License

ZebraScanSDK is distributed under the GNU General Public License.

```markdown
Infomaniak ZebraScanSDK - Android
Copyright (C) 2023 Infomaniak Network SA

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

## Contact

If you have any questions or need assistance with ZebraScanSDK, please contact our support team at [support@infomaniak.com](mailto:support@infomaniak.com).
