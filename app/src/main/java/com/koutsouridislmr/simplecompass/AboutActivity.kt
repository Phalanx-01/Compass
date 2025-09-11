package com.koutsouridislmr.simplecompass

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish() // Zurück zum Kompass
        }
    }
}