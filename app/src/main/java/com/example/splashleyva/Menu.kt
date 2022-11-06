package com.example.splashleyva

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        var aux: String? = null

        val tvWelcome = findViewById<TextView>(R.id.textView)
        val intent: Intent = intent;

        if( intent != null) {
            aux = intent.getStringExtra("usuario");
            if (aux != null && aux.isNotEmpty()) {
                tvWelcome.setText(aux);
            }
        }
    }
}