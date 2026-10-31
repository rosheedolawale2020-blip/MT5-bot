package com.rosheed.mt5bot

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(48, 96, 48, 48)
        }

        val title = TextView(this).apply {
            text = "MT5 Bot"
            textSize = 28f
        }

        val mode = TextView(this).apply {
            text = "Demo mode"
            textSize = 16f
        }

        status = TextView(this).apply {
            textSize = 16f
            setPadding(0, 48, 0, 48)
        }

        val button = Button(this).apply {
            text = "Open accessibility settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        layout.addView(title)
        layout.addView(mode)
        layout.addView(status)
        layout.addView(button)
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        status.text = if (isServiceEnabled()) {
            "Accessibility service: ON"
        } else {
            "Accessibility service: OFF"
        }
    }

    private fun isServiceEnabled(): Boolean {
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val component = ComponentName(this, BotAccessibilityService::class.java)
        val full = component.flattenToString()
        val short = component.flattenToShortString()
        return enabled.split(':').any {
            it.equals(full, ignoreCase = true) || it.equals(short, ignoreCase = true)
        }
    }
}
