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
            .setTitle("Privacy Policy / Datenschutz")
            .setMessage(getPrivacyText())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDisclaimerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer / Haftungsausschluss")
            .setMessage(getDisclaimerText())
            .setPositiveButton("Accept / Akzeptieren") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getPrivacyText(): String {
        return """
            DATENSCHUTZERKLÄRUNG / PRIVACY POLICY
            
            Stand: Januar 2024
            
            === DEUTSCH ===
            
            Datenerfassung:
            Diese App erfasst, speichert oder überträgt KEINE persönlichen Daten.
            
            Sensorzugriff:
            Die App nutzt ausschließlich lokale Gerätesensoren (Magnetometer, Accelerometer) zur Kompassfunktion. Diese Daten verlassen niemals Ihr Gerät.
            
            Internetverbindung:
            Keine Internetverbindung erforderlich. Keine Datenübertragung an externe Server.
            
            === ENGLISH ===
            
            Data Collection:
            This app does NOT collect, store, or transmit any personal data.
            
            Sensor Access:
            The app only uses local device sensors (magnetometer, accelerometer) for compass functionality. This data never leaves your device.
            
            Internet Connection:
            No internet connection required. No data transmission to external servers.
        """.trimIndent()
    }

    private fun getDisclaimerText(): String {
        return """
            HAFTUNGSAUSSCHLUSS / DISCLAIMER
            
            © 2024 Loukas Koutsouridis
            
            === WICHTIG / IMPORTANT ===
            
            Diese App dient ausschließlich zu Informationszwecken.
            This app is for informational purposes only.
            
            NICHT für kritische Navigation verwenden!
            NOT for critical navigation!
            
            === GENAUIGKEIT / ACCURACY ===
            
            Die Kompassanzeige kann beeinflusst werden durch:
            Compass readings may be affected by:
            • Metallische Gegenstände / Metal objects
            • Magnetische Felder / Magnetic fields  
            • Elektronische Geräte / Electronic devices
            • Gebäude / Buildings
            • Gerätehardware / Device hardware
            
            === HAFTUNG / LIABILITY ===
            
            Keine Haftung für:
            No liability for:
            • Ungenauigkeiten / Inaccuracies
            • Schäden jeglicher Art / Damages of any kind
            • Fehlentscheidungen / Wrong decisions
            
            NUTZUNG AUF EIGENES RISIKO
            USE AT YOUR OWN RISK
            
            Mit der Nutzung akzeptieren Sie diese Bedingungen.
            By using this app, you accept these terms.
        """.trimIndent()
    }
}