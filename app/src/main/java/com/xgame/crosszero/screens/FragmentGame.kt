package com.xgame.crosszero.screens

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.xgame.crosszero.R
import com.xgame.crosszero.screens.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*
import kotlinx.android.synthetic.main.user_layout.view.*

class FragmentGame : BaseFragment() {

    private var mode = 0
    private var first_user_score = 0
    private var second_user_score = 0
    private var mView: View? = null
    private var countDownTimer: CountDownTimer? = null
    private var time: Long = 7000

    companion object {
        private val GAME_MODE = "GAME_MODE"
        fun newInstance(gameMode: Int): FragmentGame {
            val args = Bundle()
            args.putInt(GAME_MODE, gameMode)
            val fragment = FragmentGame()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            mode = bundle.getInt(GAME_MODE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_game
    }

    override fun initView() {
        game_view.initMode(mode)
        val firstUserView = game_first_user
        val secondUserView = game_second_user
        setScore()
        setActivePlayer()

        startCount()

        if (mode == 2) {
            firstUserView.user_name.text = "You"
            firstUserView.user_img.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_user))
            firstUserView.user_figure.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_o))

            secondUserView.user_name.text = "Player 2"
            secondUserView.user_img.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_user))
            secondUserView.user_figure.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_x))


        } else if (mode == 1) {

            firstUserView.user_name.text = "You"
            firstUserView.user_img.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_user))
            firstUserView.user_figure.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_o))

            secondUserView.user_name.text = "Android"
            secondUserView.user_img.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_andr))
            secondUserView.user_figure.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.icn_x))
        }

        game_view.onChangeActiveUser = {
            setActivePlayer()
        }

        game_view.onResetGameMsg = {
            Toast.makeText(context, "Draw!", Toast.LENGTH_SHORT).show()
        }

        game_view.onScoreChange = {
            if (it == 0) {
                first_user_score++
            } else if (it == 1) {
                second_user_score++
            }

            checkScore()
        }

        game_view.onResetGame = {
            countDownTimer?.start()
        }

        game_back_iv.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    private fun checkScore() {
        if (first_user_score > 5) {
            goToFragment(FragmentWinGame.newInstance(mode, 0))
        } else if (second_user_score > 5) {
            goToFragment(FragmentWinGame.newInstance(mode, 1))
        } else if (first_user_score == 5 && second_user_score == 5) {
            goToFragment(FragmentWinGame.newInstance(mode, -1))
        } else {
            setScore()
        }
    }

    override fun goToFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction()
            .apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.content_container, fragment)
                commit()
            }
    }

    private fun setScore() {
        game_score_tv.text = "$first_user_score:$second_user_score"
    }

    private fun setActivePlayer() {
        if (game_view.drawX) {
            game_first_user_active_point.visibility = View.INVISIBLE
            game_second_user_active_point.visibility = View.VISIBLE
        } else {
            game_first_user_active_point.visibility = View.VISIBLE
            game_second_user_active_point.visibility = View.INVISIBLE
        }
    }

    private fun startCount() {

        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                mView?.game_view?.resetGame()
                time = 7000
                resetCountDown()
                refreshGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                mView?.game_countdown_tv?.text = ((millisUntilFinished - 1000) / 1000).toString()
            }

        }.start()
    }

    private fun refreshGame() {
        val isX = (mView?.game_view?.drawX!!)
        mView?.game_view?.drawX = !(isX)
        if (isX){
            first_user_score++
        } else{
            second_user_score++
        }
        checkScore()
        setActivePlayer()
    }

    private fun resetCountDown(){
        countDownTimer?.start()
    }

    override fun onResume() {
        super.onResume()
        resetCountDown()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()

    }
}