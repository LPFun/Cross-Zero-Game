package com.xgame.crosszero.custom

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.xgame.crosszero.R
import kotlin.math.abs

class Logo @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, deffStyleAttr: Int = 0) :
    View(context, attributeSet, deffStyleAttr) {
    private val mContext = context
    private val x = BitmapFactory.decodeResource(mContext.resources, R.drawable.icn_xb)
    private val o = BitmapFactory.decodeResource(mContext.resources, R.drawable.icn_o_b)
    private val mPaint = Paint()
    private var mHeight = 0
    private var mWidth = 0
    private var destRectX = Rect(0, 0, 0, 0)
    private var destRectO = Rect(0, 0, 0, 0)
    private var side = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        side = Math.min(mHeight, mWidth)

        val delta = abs(mWidth - mHeight)/2

        destRectX.left = delta
        destRectX.top = 0
        destRectX.bottom = side
        destRectX.right = mWidth - delta

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(x, null, destRectX, mPaint)
        canvas?.drawBitmap(o, null, destRectX, mPaint)
    }
}