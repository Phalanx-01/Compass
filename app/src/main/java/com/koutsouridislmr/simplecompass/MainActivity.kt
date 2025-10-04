package com.koutsouridislmr.simplecompass

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.round

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var compassImage: ImageView
    private lateinit var degreeText: TextView
    private lateinit var directionText: TextView
    private lateinit var calibrationText: TextView
    private lateinit var exitButton: Button
    private lateinit var aboutButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private var currentDegree = 0f
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private var lastUpdateTime = 0L

    companion object {
        private const val PREFS_NAME = "CompassPrefs"
        private const val KEY_DISCLAIMER_SHOWN = "disclaimer_shown"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Views initialisieren
        compassImage = findViewById(R.id.compassImage)
        degreeText = findViewById(R.id.degreeText)
        directionText = findViewById(R.id.directionText)
        calibrationText = findViewById(R.id.calibrationText)
        exitButton = findViewById(R.id.exitButton)
        aboutButton = findViewById(R.id.aboutButton)

        // Exit Button Click Listener
        exitButton.setOnClickListener {
            finish()
        }

        // About Button Click Listener
        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // Sensor Manager initialisieren
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // Check if sensors are available
        if (accelerometer == null || magnetometer == null) {
            degreeText.text = "N/A"
            directionText.text = "?"
            calibrationText.text = "⚠ No sensors found"
            calibrationText.setTextColor(getColor(R.color.red))
        }

        // Show disclaimer on first launch
        if (!sharedPreferences.getBoolean(KEY_DISCLAIMER_SHOWN, false)) {
            showDisclaimerDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Sensoren registrieren
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        // Sensoren deregistrieren um Batterie zu sparen
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        // Limitiere Updates auf 30 FPS für flüssigere Animation
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime < 33) return
        lastUpdateTime = currentTime

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, gravity, 0, event.values.size)
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
            }
        }

        val rotationMatrix = FloatArray(9)
        val inclinationMatrix = FloatArray(9)

        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            inclinationMatrix,
            gravity,
            geomagnetic
        )

        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            // Azimuth in Grad umwandeln
            var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            azimuth = (azimuth + 360) % 360

            // Smooth Animation erstellen
            val rotateAnimation = RotateAnimation(
                currentDegree,
                -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 250
                fillAfter = true
                interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            }

            // Animation starten
            compassImage.startAnimation(rotateAnimation)
            currentDegree = -azimuth

            // Grad anzeigen (nur die Zahl, ohne °)
            val degree = round(azimuth).toInt()
            degreeText.text = "$degree"

            // Richtung bestimmen und anzeigen
            val direction = getDirection(degree)
            directionText.text = direction

            // Richtungsfarbe und Hintergrund basierend auf Himmelsrichtung
            when (direction) {
                "N" -> {
                    directionText.setBackgroundResource(R.drawable.circle_north)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "NE" -> {
                    directionText.setBackgroundResource(R.drawable.circle_northeast)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "E" -> {
                    directionText.setBackgroundResource(R.drawable.circle_east)
                    directionText.setTextColor(getColor(R.color.black))
                }
                "SE" -> {
                    directionText.setBackgroundResource(R.drawable.circle_southeast)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "S" -> {
                    directionText.setBackgroundResource(R.drawable.circle_south)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "SW" -> {
                    directionText.setBackgroundResource(R.drawable.circle_southwest)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "W" -> {
                    directionText.setBackgroundResource(R.drawable.circle_west)
                    directionText.setTextColor(getColor(R.color.white))
                }
                "NW" -> {
                    directionText.setBackgroundResource(R.drawable.circle_northwest)
                    directionText.setTextColor(getColor(R.color.white))
                }
                else -> {
                    directionText.setBackgroundResource(R.drawable.circle_background)
                    directionText.setTextColor(getColor(R.color.white))
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Update calibration status
        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                calibrationText.text = "✓ Perfectly calibrated"
                calibrationText.setTextColor(getColor(R.color.green))
            }
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                calibrationText.text = "✓ Calibrated"
                calibrationText.setTextColor(getColor(R.color.green))
            }
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                calibrationText.text = "⚠ Calibration needed"
                calibrationText.setTextColor(getColor(R.color.accent))
            }
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                calibrationText.text = "⚠ Please move in figure-8"
                calibrationText.setTextColor(getColor(R.color.red))
            }
        }
    }

    private fun getDirection(degree: Int): String {
        return when (degree) {
            in 0..22, in 338..360 -> "N"
            in 23..67 -> "NE"  // Northeast (statt NO)
            in 68..112 -> "E"
            in 113..157 -> "SE" // Southeast (statt SO)
            in 158..202 -> "S"
            in 203..247 -> "SW" // Southwest
            in 248..292 -> "W"
            in 293..337 -> "NW" // Northwest
            else -> ""
        }
    }

    private fun showDisclaimerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer")
            .setMessage(getDisclaimerText())
            .setPositiveButton("Accept") { dialog, _ ->
                // Mark disclaimer as shown
                sharedPreferences.edit().putBoolean(KEY_DISCLAIMER_SHOWN, true).apply()
                dialog.dismiss()
            }
            .setNegativeButton("Decline") { _, _ ->
                // If user declines, exit the app
                finish()
            }
            .setCancelable(false) // User must choose Accept or Decline
            .show()
    }

    private fun getDisclaimerText(): String {
        return """
            IMPORTANT NOTICE

            This app is for informational purposes only and should NOT be used for critical navigation.

            Compass readings may be affected by:
            • Metal objects
            • Magnetic fields
            • Electronic devices
            • Buildings
            • Device hardware limitations

            No liability is assumed for:
            • Inaccuracies in compass readings
            • Any damages or losses
            • Decisions made based on this app

            This compass should not be relied upon for:
            • Emergency situations
            • Professional navigation
            • Life-critical decisions

            USE AT YOUR OWN RISK

            By accepting, you acknowledge that you understand and agree to these terms.
        """.trimIndent()
    }
}