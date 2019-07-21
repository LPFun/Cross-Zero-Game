package com.xgame.crosszero.screens

import androidx.fragment.app.Fragment
import com.xgame.crosszero.R
import com.xgame.crosszero.screens.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_registration.*

class FragmentRegistration : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_registration
    }

    override fun initView() {
        registration_continue_btn.setOnClickListener{goToFragment(FragmentGameMenu())}
    }

    override fun goToFragment(fragment: Fragment) {
        fragmentManager!! 
            .beginTransaction()
            .apply {
                replace(R.id.content_container, fragment)
                commit()
            }
    }
}