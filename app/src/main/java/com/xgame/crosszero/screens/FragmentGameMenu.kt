package com.xgame.crosszero.screens

import com.xgame.crosszero.R
import com.xgame.crosszero.screens.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_game_menu.*

class FragmentGameMenu : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_game_menu
    }

    override fun initView() {
        game_menu_singleplayer_btn.setOnClickListener({
            goToFragment(FragmentGame.newInstance(1))
        })
        game_menu_muliplayer_btn.setOnClickListener {
            goToFragment(FragmentGame.newInstance(2))
        }
    }
}