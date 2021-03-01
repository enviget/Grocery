package com.example.grocery.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.grocery.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        init()
    }

    private fun init() {
        var handler = Handler()
        var thread = Thread(){
            kotlin.run{
                for(i in 3 downTo 1){
                    Thread.sleep(1000)
                    handler.post{splash_screen.text = "$i"}
                }
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}