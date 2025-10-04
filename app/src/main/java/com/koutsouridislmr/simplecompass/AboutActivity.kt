package com.koutsouridislmr.simplecompass

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button

class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton: Button = findViewById(R.id.backButton)
        val privacyButton: Button = findViewById(R.id.privacyButton)
        val disclaimerButton: Button = findViewById(R.id.disclaimerButton)

        backButton.setOnClickListener {
            finish()
        }

        privacyButton.setOnClickListener {
            showPrivacyDialog()
        }

        disclaimerButton.setOnClickListener {
            showDisclaimerDialog()
        }
    }

    private fun showPrivacyDialog() {
        AlertDialog.Builder(this)
            .setTitle("Privacy Policy")
            .setMessage(getPrivacyText())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDisclaimerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer")
            .setMessage(getDisclaimerText())
            .setPositiveButton("Accept") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getPrivacyText(): String {
        return """
            PRIVACY POLICY

            Last Updated: January 2024

            Data Collection:
            This app does NOT collect, store, or transmit any personal data.

            Sensor Access:
            The app only uses local device sensors (magnetometer, accelerometer) for compass functionality. This data never leaves your device.

            Internet Connection:
            No internet connection required. No data transmission to external servers.

            Your Privacy:
            • No personal information is collected
            • No location data is stored
            • No analytics or tracking
            • Completely offline operation
            • All sensor data remains on your device
        """.trimIndent()
    }

    private fun getDisclaimerText(): String {
        return """
            DISCLAIMER

            © 2024 Loukas Koutsouridis

            === IMPORTANT ===

            This app is for informational purposes only.
            NOT for critical navigation!

            === ACCURACY ===

            Compass readings may be affected by:
            • Metal objects
            • Magnetic fields
            • Electronic devices
            • Buildings
            • Device hardware limitations

            === LIABILITY ===

            No liability for:
            • Inaccuracies in compass readings
            • Damages of any kind
            • Decisions made based on this app
            • Navigation errors

            === TERMS OF USE ===

            USE AT YOUR OWN RISK

            This compass is a simple tool and should not be relied upon for:
            • Emergency situations
            • Professional navigation
            • Life-critical decisions

            By using this app, you accept these terms and conditions.
        """.trimIndent()
    }
}