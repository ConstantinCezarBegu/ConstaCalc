package com.constaapps.constacalc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.constaapps.constacalc.ui.main.MainFragment



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance())
                .commitNow()
        }
    }
}
