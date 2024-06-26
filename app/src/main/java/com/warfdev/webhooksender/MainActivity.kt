package com.warfdev.webhooksender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.warfdev.webhooksender.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var editTextWebhookURL: EditText
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendWebhook: Button
    private lateinit var buttonGoNews: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextWebhookURL = findViewById(R.id.editTextWebhookURL)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendWebhook = findViewById(R.id.buttonSendWebhook)
        buttonGoNews = findViewById(R.id.buttonGoNews)

        val settingsButton: Button = findViewById(R.id.buttonSettings)
        settingsButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        val newsButton: Button = findViewById(R.id.buttonGoNews)
         newsButton.setOnClickListener {
            val intent = Intent(this@MainActivity, IntroActivity::class.java)
            startActivity(intent)
        }

        var sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Kaydedilen webhook URL değerini al
        val savedWebhookUrl = getSavedWebhookUrl()
        val savedWebhookName = getSavedWebhookName()

        // EditText referansını al ve içeriğini değiştir
        editTextWebhookURL.setText(savedWebhookUrl)

        buttonSendWebhook.setOnClickListener {
            val webhookURL = editTextWebhookURL.text.toString()
            val message = editTextMessage.text.toString()

            if (webhookURL.isNotEmpty() && message.isNotEmpty()) {
                sendWebhook(webhookURL, message)
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSavedWebhookUrl(): String {
        var sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("webhook_url", "") ?: ""
    }
    private fun getSavedWebhookName(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedName = sharedPreferences.getString("webhook_name", "")

        return if (savedName.isNullOrBlank()) {
            "Sent via waHooks App" // ayarlı değil ise
        } else {
            savedName // ayarlı ise
        }
    }

    /**
     * @description DiscordAPI Webhook JSON payload'ı;
     * canim controbutorum verdi askim benim
    {
    "content": "Merhaba!",
    "username": "wahooks",
    "avatar_url": "https://example.com/avatar.png",
    "embeds": [
    {
    "title": "Bu bir embed başlığıdır",
    "description": "Bu bir embed açıklamasıdır.",
    "color": 16711680, // bu color sistemini boyle yapanin anasini #HEX DICEZ KULLANCAZ ISTE BUEQ
    "fields": [
    {
    "name": "Alan 1",
    "value": "Değer 1"
    },
    {
    "name": "Alan 2",
    "value": "Değer 2"
    }
    ]
    }
    ]
    }

     */

    private fun sendWebhook(webhookURL: String, message: String) {
        val client = OkHttpClient()
        val mediaTypeString = "application/json" // veya istediğiniz medya türü
        val json = "{\"username\":\"${getSavedWebhookName()}\", \"content\":\"$message\"}"
        val mediaType = mediaTypeString.toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, json)
        val request = Request.Builder()
            .url(webhookURL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Webhook gönderme başarısız", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Webhook başarıyla gönderildi", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Webhook gönderme başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}