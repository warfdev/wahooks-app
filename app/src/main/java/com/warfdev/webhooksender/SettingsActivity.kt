package com.warfdev.webhooksender
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    private lateinit var webhookUrlEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        webhookUrlEditText = findViewById(R.id.optionsWebhookURL)

        val savedWebhookUrl = sharedPreferences.getString("webhook_url", "")
        webhookUrlEditText.setText(savedWebhookUrl)

        val saveButton: Button = findViewById(R.id.optionsSave)
        saveButton.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {
        val webhookUrl = webhookUrlEditText.text.toString()

        val editor = sharedPreferences.edit()
        editor.putString("webhook_url", webhookUrl)
        editor.apply()

        Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show()
    }
}