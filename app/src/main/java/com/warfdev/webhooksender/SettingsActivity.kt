package com.warfdev.webhooksender
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    private lateinit var webhookUrlEditText: EditText
    private lateinit var webhookEditName: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton: Button = findViewById(R.id.buttonSettings)
        backButton.setOnClickListener {
            onBackPressed() // Geri butonuna basılınca varsayılan geri işlevi çağrılır
        }

        val secondButton: Button = findViewById(R.id.optionsGoSecond)
        secondButton.setOnClickListener {
            val intent = Intent(this@SettingsActivity, SecondActivity::class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        webhookUrlEditText = findViewById(R.id.optionsWebhookURL)
        webhookEditName = findViewById(R.id.optionsWebhookName)

        val savedWebhookUrl = sharedPreferences.getString("webhook_url", "")
        webhookUrlEditText.setText(savedWebhookUrl)

        val savedWebhookName = sharedPreferences.getString("webhook_name", "")
        webhookEditName.setText(savedWebhookName)

        val saveButton: Button = findViewById(R.id.optionsSave)
        saveButton.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {
        val webhookUrl = webhookUrlEditText.text.toString()
        val webhookName = webhookEditName.text.toString()

        val editor = sharedPreferences.edit()
        editor.putString("webhook_url", webhookUrl)
        editor.putString("webhook_name", webhookName)
        editor.apply()

        Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show()
    }
}