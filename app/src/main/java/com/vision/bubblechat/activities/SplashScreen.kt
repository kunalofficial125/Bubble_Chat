package com.vision.bubblechat.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, 0)
            insets
        }

        val isLoggedIn = getSharedPreferences("account", MODE_PRIVATE).getBoolean("isLoggedIn", false)

        Handler().postDelayed({
            if (isLoggedIn) {
                val toHome = Intent(
                    this@SplashScreen,
                    MainActivity::class.java
                )
                startActivity(toHome)
                finish()
            } else {
                val toLogin: Intent = Intent(
                    this@SplashScreen,
                    InitialActivity::class.java
                )
                startActivity(toLogin)
                finish()
            }
        }, 2000)

    }
}