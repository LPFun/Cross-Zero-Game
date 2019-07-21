package com.xgame.crosszero.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.xgame.crosszero.R

class GameManager(val context: Context) {

    var gameMode = 1

    fun getO(): Bitmap{
        return getBitmap(R.drawable.icn_o_b)
    }

    fun getX(): Bitmap{
        return getBitmap(R.drawable.icn_xb)
    }

    private fun getBitmap(id: Int) : Bitmap{
        return BitmapFactory.decodeResource(context.resources, id)
    }
}