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


class IntroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val backButton: Button = findViewById(R.id.buttonIntroBack)
        backButton.setOnClickListener {
            onBackPressed() // Geri butonuna basılınca varsayılan geri işlevi çağrılır
        }



    }


}