package com.example.prokeyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rec.apply {
            adapter = Adapter(mutableListOf(
                Model("Go to Central Park"),
                Model("Buy new macbook"),
                Model("Get feedback on website design"),
                Model("Buy milk"),
                Model("Call Katherine about the trip")
            ))
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}