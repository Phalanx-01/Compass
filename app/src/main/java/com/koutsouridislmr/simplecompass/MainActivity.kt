package com.koutsouridislmr.simplecompass

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.round

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var compassImage: ImageView
    private lateinit var degreeText: TextView
    private lateinit var directionText: TextView

    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private var currentDegree = 0f
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views initialisieren
        compassImage = findViewById(R.id.compassImage)
        degreeText = findViewById(R.id.degreeText)
        directionText = findViewById(R.id.directionText)

        // Sensor Manager initialisieren
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (accelerometer == null || magnetometer == null) {
            degreeText.text = "Keine Sensoren"
            directionText.text = "Emulator?"
        }

    }

    override fun onResume() {
        super.onResume()
        // Sensoren registrieren
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Sensoren deregistrieren um Batterie zu sparen
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
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

            // Animation erstellen
            val rotateAnimation = RotateAnimation(
                currentDegree,
                -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 200
                fillAfter = true
            }

            // Animation starten
            compassImage.startAnimation(rotateAnimation)
            currentDegree = -azimuth

            // Grad anzeigen
            val degree = round(azimuth).toInt()
            degreeText.text = "$degree°"

            // Richtung bestimmen und anzeigen
            directionText.text = getDirection(degree)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Nicht benötigt für diese App
    }

    private fun getDirection(degree: Int): String {
        return when (degree) {
            in 0..22, in 338..360 -> "N"
            in 23..67 -> "NO"
            in 68..112 -> "O"
            in 113..157 -> "SO"
            in 158..202 -> "S"
            in 203..247 -> "SW"
            in 248..292 -> "W"
            in 293..337 -> "NW"
            else -> ""
        }
    }
}