package com.vision.bubblechat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.vision.bubblechat.activities.InitialActivity
import com.vision.bubblechat.fragments.HomeScreen
import com.vision.bubblechat.fragments.LoginPage
import com.vision.bubblechat.repositories.UsersRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0,0,0,0)
            insets
        }

        if(FirebaseAuth.getInstance().currentUser != null){
            replaceFragmentWithoutBackStack(HomeScreen(), Bundle(), "Home Screen")
        }
        else{
            startActivity(Intent(this, InitialActivity::class.java))
            finish()
        }

        if(savedInstanceState == null){
            replaceFragmentWithoutBackStack(HomeScreen(), Bundle(), "Home Screen")
        }

        savingUsernameLocally()

    }

    fun replaceFragmentWithoutBackStack(newFrag: Fragment, bundle: Bundle, tag:String) {
        supportFragmentManager.beginTransaction().
        replace(R.id.main,newFrag::class.java,bundle).commit()
    }

    fun replaceFragment(newFrag: Fragment, bundle: Bundle, tag:String) {
        supportFragmentManager.beginTransaction().
        replace(R.id.main,newFrag::class.java,bundle).addToBackStack(tag).commit()
    }

    private fun savingUsernameLocally(){
        val sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        UsersRepository().getUsername(FirebaseAuth.getInstance().currentUser?.email!!){
            editor.putString("username", it)
            editor.apply()
        }
    }

}