package com.xgame.crosszero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xgame.crosszero.custom.GameView
import com.xgame.crosszero.screens.FragmentGame
import com.xgame.crosszero.screens.FragmentGameMenu
import com.xgame.crosszero.screens.FragmentRegistration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goToFragment()
    }

    private fun goToFragment() {
        supportFragmentManager
            .beginTransaction()
            .apply {
                replace(R.id.content_container, FragmentGameMenu())
                commit()
            }
    }
}
