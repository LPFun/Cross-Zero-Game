package com.xgame.crosszero.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.xgame.crosszero.R
import com.xgame.crosszero.screens.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_win_game.*

class FragmentWinGame : BaseFragment() {
    private var win_mode = 1
    private var game_mode = 0


    companion object {
        private val WIN_MODE = "WIN_MODE"
        private val GAME_MODE = "GAME_MODE"

        fun newInstance(game_mode : Int, win_mode : Int):FragmentWinGame{
            val args = Bundle()
            args.putInt(WIN_MODE, win_mode)
            args.putInt(GAME_MODE, game_mode)
            val fragment = FragmentWinGame()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null) {
            win_mode = bundle.getInt(WIN_MODE)
            game_mode = bundle.getInt(GAME_MODE)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_win_game
    }

    override fun initView() {
        when(win_mode){
            -1 -> win_tv.text = "Draw!"
            0 -> win_tv.text = "You winner!"
            1 -> win_tv.text = "Player 2 winner!"
        }

        win_again_imv.setOnClickListener {
            goToFragment(FragmentGame.newInstance(game_mode))
        }

        win_menu_imv.setOnClickListener {
            goToFragment(FragmentGameMenu())
        }

    }

    override fun goToFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction()
            .apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                replace(R.id.content_container, fragment)
                commit()
            }
    }
}