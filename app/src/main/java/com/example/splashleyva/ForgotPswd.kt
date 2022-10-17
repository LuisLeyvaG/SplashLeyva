package com.example.splashleyva

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ForgotPswd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pswd)


    }

     fun goLogin(v: View) {
        val intent = Intent(this@ForgotPswd, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}