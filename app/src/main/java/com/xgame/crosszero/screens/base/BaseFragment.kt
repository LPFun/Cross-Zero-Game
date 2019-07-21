package com.xgame.crosszero.screens.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.xgame.crosszero.R
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class BaseFragment : Fragment() {

    @LayoutRes protected abstract fun getLayoutId() : Int
    protected abstract fun initView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    open fun goToFragment(fragment: Fragment){
        fragmentManager!!
            .beginTransaction()
            .apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.content_container, fragment)
                addToBackStack(null)
                commit()
            }
    }
    open fun backToFragment(){
        fragmentManager!!.popBackStack()
    }
}