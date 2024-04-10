package com.warfdev.webhooksender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextColor = findViewById<EditText>(R.id.editTextColor)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val editTextFooter = findViewById<EditText>(R.id.editTextFooter)
        val buttonSend = findViewById<Button>(R.id.buttonSend)

        val backButton: ImageButton = findViewById(R.id.buttonSettings)
        backButton.setOnClickListener {
            onBackPressed()
        }

        buttonSend.setOnClickListener {
            val title = editTextTitle.text.toString()
            val color = editTextColor.text.toString()
            val description = editTextDescription.text.toString()
            val footer = editTextFooter.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Title ve Description alanları boş bırakılamaz!", Toast.LENGTH_SHORT).show()
            } else {
                val webhookUrl = getSavedWebhookUrl()
                if (webhookUrl.isNotEmpty()) {
                    sendWebhook(title, color, description, footer, webhookUrl)
                } else {
                    Toast.makeText(this, "Webhook URL'si bulunamadı", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ayarlar'a kayit edilmiş dcWebhook URL sini çekiyoruz
    private fun getSavedWebhookUrl(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("webhook_url", "") ?: ""
    }

    // kayitli webhook uzerinden ivj yapiyoruz
    private fun sendWebhook(title: String, color: String, description: String, footer: String, webhookUrl: String) {
        // tanımlanmış color code'lar
        // -- ontabanli kodlar icin eyw astro08
        var colorCode = color
        if (color.equals("RED", ignoreCase = true)) {
            colorCode = "16711680"
        } else if (color.equals("BLUE", ignoreCase = true)) {
            colorCode = "255"
        } else if (color.equals("LIGHT_BLUE", ignoreCase = true)) {
            colorCode = "12582911"
        } else if (color.equals("GRAY", ignoreCase = true)) {
            colorCode = "8421504"
        } else if (color.equals("GREEN", ignoreCase = true)) {
            colorCode = "32768"
        } else if (color.equals("YELLOW", ignoreCase = true)) {
            colorCode = "16776960"
        } else if (color.equals("PURPLE", ignoreCase = true)) {
            colorCode = "8388736"
        } else if (color.equals("LIGHT_GREEN", ignoreCase = true)) {
            colorCode = "8454143"
        } else if (color.equals("LIGHT_PURPLE", ignoreCase = true)) {
            colorCode = "13408767"
        } else if (color.equals("ORANGE", ignoreCase = true)) {
            colorCode = "16753920"
        }

        /*

        Registered Embed Color Codes List

        - [added]: RED
        - [added]: BLUE
        - [added]: GRAY
        - [added]: GREEN
        - [added]: YELLOW
        - [added]: PURPLE
        - [added]: ORANGE
        - [added]: LIGHT_BLUE
        - [added]: LIGHT_GREEN
        - [added]: LIGHT_PURPLE

         */

        // object payload'ı
        // sorun cozuldu komtanm -- contributr: astro08
        //
        val payload = """
            {
                "embeds": [
                    {
                        "title": "$title",
                        "description": "$description",
                        "color": "$colorCode",
                        "footer": {
                            "text": "$footer"
                        }
                    }
                ]
            }
        """.trimIndent()


        // http send yapacas krds
        val client = OkHttpClient()
        val mediaTypeString = "application/json"
        val json = payload
        val mediaType = mediaTypeString.toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(webhookUrl)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Webhook gönderme başarısız ${e.message}", Toast.LENGTH_SHORT).show()
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