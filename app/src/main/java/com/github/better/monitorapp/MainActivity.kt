package com.github.better.monitorapp

import android.content.Intent
import android.os.BaseBundle
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.better.monitorapp.extension.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_a.setOnClickListener {
            startActivity(Intent(applicationContext, AActivity::class.java))
        }

        btn_b.setOnClickListener {
            startActivity(Intent(applicationContext, BActivity::class.java))
        }

        btn_c.setOnClickListener {
            startActivity(Intent(applicationContext, CActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
