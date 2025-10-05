# SimpleCompass 🧭

A clean, privacy-focused digital compass app for Android that provides accurate directional information using device sensors.

## Features

### Core Functionality
- **Real-time Compass Display**: Smooth, animated compass rose that rotates based on device orientation
- **Degree Precision**: Shows exact heading in degrees (0-360°)
- **Cardinal Directions**: Clear display of N, NE, E, SE, S, SW, W, NW with color-coded indicators
- **Calibration Status**: Real-time sensor calibration monitoring with user guidance
- **Smooth Animations**: 30 FPS refresh rate for fluid compass movement

### Privacy & Security
- **100% Offline**: No internet connection required or used
- **Zero Data Collection**: No analytics, tracking, or personal data collection
- **No Permissions Abuse**: Only requests necessary sensor permissions
- **Transparent**: Open source with clear privacy policy

### User Experience
- **Portrait Mode Lock**: Consistent interface orientation
- **First-Launch Disclaimer**: Important usage information for new users
- **About Section**: Easy access to privacy policy and disclaimer
- **Clean Material Design**: Simple, intuitive interface

## Screenshots

| Main Compass View | Calibration Status | About Screen |
|------------------|-------------------|--------------|
| ![Compass](sampledata/screenshot1.png) | ![Calibration](sampledata/screenshot2.png) | ![About](sampledata/screenshot3.png) |

## Requirements

- Android 7.0 (API level 24) or higher
- Device with magnetometer (compass) sensor
- Device with accelerometer sensor

## Installation

### From Source

1. Clone the repository:
```bash
git clone https://github.com/yourusername/SimpleCompass.git
cd SimpleCompass
```

2. Open in Android Studio:
   - File → Open → Select the SimpleCompass directory
   - Wait for Gradle sync to complete

3. Build and run:
```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### APK Installation

1. Download the latest APK from the [Releases](https://github.com/yourusername/SimpleCompass/releases) page
2. Enable "Install from Unknown Sources" in your device settings
3. Open the APK file to install

## Development

### Project Structure
```
SimpleCompass/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/koutsouridislmr/simplecompass/
│   │   │   │   ├── MainActivity.kt      # Main compass logic
│   │   │   │   └── AboutActivity.kt     # About/info screen
│   │   │   ├── res/                     # Resources (layouts, drawables, values)
│   │   │   └── AndroidManifest.xml      # App configuration
│   │   └── test/                        # Unit tests
│   └── build.gradle.kts                 # App-level build config
├── gradle/
│   └── libs.versions.toml               # Version catalog
└── build.gradle.kts                     # Project-level build config
```

### Building

```bash
# Clean build
./gradlew clean build

# Run tests
./gradlew test

# Run lint checks
./gradlew lint

# Generate signed APK
./gradlew assembleRelease
```

### Key Technologies
- **Kotlin** 2.0.21
- **Android SDK** 36 (Android 14)
- **Min SDK** 24 (Android 7.0)
- **Gradle** 8.13.0
- **AndroidX Libraries**

## How It Works

The compass uses sensor fusion to determine device orientation:

1. **Accelerometer** provides gravity vector data
2. **Magnetometer** provides magnetic field vector data
3. **Sensor Fusion** combines both inputs using `SensorManager.getRotationMatrix()`
4. **Azimuth Calculation** extracts heading from rotation matrix
5. **UI Update** animates compass rose and updates degree/direction displays

### Calibration

For accurate readings, the magnetometer may need calibration:
- ✅ **Green indicator**: Well calibrated
- ⚠️ **Yellow indicator**: Calibration needed
- ❌ **Red indicator**: Move device in figure-8 pattern

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Disclaimer

⚠️ **Important**: This compass app is for informational purposes only and should NOT be relied upon for critical navigation, emergency situations, or life-critical decisions.

Compass accuracy can be affected by:
- Magnetic interference from metal objects
- Electronic devices
- Building structures
- Device hardware limitations

## Author

**Loukas Koutsouridis**

## Acknowledgments

- Android sensor documentation and best practices
- Material Design guidelines
- Open source community

---

*Privacy First • No Ads • No Tracking • Just a Compass*