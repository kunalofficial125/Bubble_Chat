package com.vision.bubblechat.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.vision.bubblechat.R
import com.vision.bubblechat.fragments.LoginPage

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0,0,0,0)
            insets
        }

        if(savedInstanceState == null){
            replaceFragmentWithoutBackStack(LoginPage(), Bundle(), "Login Page")
        }

    }

    fun replaceFragmentWithoutBackStack(newFrag: Fragment, bundle: Bundle, tag:String) {
        supportFragmentManager.beginTransaction().
        replace(R.id.main,newFrag::class.java,bundle).commit()
    }


    fun replaceFragment(newFrag: Fragment, bundle: Bundle, tag:String) {
        supportFragmentManager.beginTransaction().
        replace(R.id.main,newFrag::class.java,bundle).addToBackStack(tag).commit()
    }

}