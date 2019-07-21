package com.xgame.crosszero.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.xgame.crosszero.game.GameCell
import com.xgame.crosszero.game.GameManager
import kotlin.math.min

class GameView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, deffStyleAttr: Int = 0) :
    View(context, attributeSet, deffStyleAttr) {
    private val mContext = context
    private var mHeight = 0
    private var mWidth = 0
    private var paintGrid = Paint()
    private var paintWinLine = Paint()
    private var sideSize = 0f
    private val gameManager = GameManager(mContext)
    private var X = gameManager.getX()
    private var O = gameManager.getO()
    private var curCol = 0
    private var curRow = 0
    private var winLineStartX = 0f
    private var winLineStartY = 0f
    private var winLineEndX = 0f
    private var winLineEndY = 0f
    private var drawSymbol = false
    private var symbolRctSrc = Rect(0, 0, X.width, X.height)
    private var symbolRctDst = Rect(0, 0, X.width, X.height)
    private var cellCoordinatersList = Array(3, { Array(3, { GameCell() }) })
    var drawX = false
    private var drawWinLine = false
    private var arrOfGameCells = ArrayList<GameCell>()
    var onChangeActiveUser: () -> Unit = {}
    var onScoreChange: (symbol: Int) -> Unit = {}
    var onResetGameMsg: () -> Unit = {}
    var onResetGame: () -> Unit = {}
    private var gameMode = 1

    fun initMode(mode: Int) {
        gameMode = mode
        with(paintGrid) {
            color = Color.WHITE
            strokeWidth = 2f
        }
        with(paintWinLine) {
            color = Color.WHITE
            strokeWidth = 8f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        sideSize = (min(mHeight / 3, mWidth / 3)).toFloat()
        initCellCentersList(sideSize)
    }

    private fun initCellCentersList(sideSize: Float) {
        for (i in 0..2) {
            for (j in 0..2) {
                val gameCell = GameCell()
                gameCell._x = (sideSize * i).toInt()
                gameCell._y = (sideSize * j).toInt()
                cellCoordinatersList[i][j] = gameCell
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawField(canvas)
        if (drawSymbol) {
            drawSymbolOnCanvas(canvas)
            drawSymbol = false
        }

        if (drawWinLine) {
            drawWinLine(canvas)
            drawWinLine = false
            resetGame()
        }
    }

    private fun drawWinLine(canvas: Canvas) {
        canvas.drawLine(winLineStartX, winLineStartY, winLineEndX, winLineEndY, paintWinLine)
    }

    private fun drawSymbolOnCanvas(canvas: Canvas) {
        for (gameCell in arrOfGameCells) {
            val _x = gameCell._x
            val _y = gameCell._y

            with(symbolRctDst) {
                left = _x
                top = _y
                right = _x + sideSize.toInt()
                bottom = _y + sideSize.toInt()
            }
            canvas.drawBitmap(if (gameCell.figure == 1) X else O, symbolRctSrc, symbolRctDst, paintGrid)
        }

        if (gameMode == 1) {
            Handler().postDelayed({
                if (arrOfGameCells.size % 2 == 1) {
                    androidMove()
                }
            }, 500)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        if (event.action == MotionEvent.ACTION_DOWN) {
            drawSymbol(x, y)

        }
        return true
    }

    private fun androidMove() {
        checkOnRepeatAndDraw((0..2).random(), (0..2).random())
    }

    private fun drawSymbol(x: Float, y: Float) {
        checkOnRepeatAndDraw(getCellIndex(x), getCellIndex(y))
    }

    private fun checkOnRepeatAndDraw(r: Int, c: Int) {
        val gameCell = arrOfGameCells.firstOrNull {
            if (r.equals(it.row) && c.equals(it.col)) {
                true
            } else {
                false
            }
        }

        if (gameCell == null) {
            curRow = r
            curCol = c
            addGameCell()
        } else if (gameMode == 1) {
            androidMove()
        }
    }

    private fun addGameCell() {
        for (i in 0..2) {
            for (j in 0..2) {
                if (i == curRow && j == curCol) {

                    val gameCell = cellCoordinatersList[i][j]

                    gameCell.row = i
                    gameCell.col = j

                    gameCell.figure = if (drawX) 1 else 0

                    drawX = !drawX
                    onChangeActiveUser()

                    arrOfGameCells.add(gameCell)

                    onResetGame()

                    checkOnEndGame2()

                    drawSymbol = true
                    postInvalidate(
                        gameCell._x,
                        gameCell._y,
                        gameCell._x + sideSize.toInt(),
                        gameCell._y + sideSize.toInt()
                    )
                    return
                }
            }
        }
    }

    fun resetGame() {
        if (gameMode == 1) {
            drawX = false
            onChangeActiveUser()
        }
        arrOfGameCells = ArrayList()
        initCellCentersList(sideSize)
        winLineStartX = 0f
        winLineStartY = 0f
        winLineEndX = 0f
        winLineEndY = 0f
        Handler().postDelayed({
            postInvalidate()
        }, 1000)
    }

    private fun checkOnEndGame2() {
        var winCol = false
        var winRow = false
        var winDiagLeft = false
        var winDiagRight = false

        if (arrOfGameCells.size >= 5) {
            val lastGameCell = arrOfGameCells.last()
            val col = lastGameCell.col
            val row = lastGameCell.row
            var arrOCountHor = 0
            var arrXCountHor = 0
            var arrOCountVer = 0
            var arrXCountVer = 0
            var arrOCountDiagLeft = 0
            var arrXCountDiagRight = 0

            for (i in 0..2) {
                if (cellCoordinatersList[i][col].figure == 0) {
                    arrOCountHor++
                    if (arrOCountHor == 3) {
                        winCol = true
                    }
                }
                if (cellCoordinatersList[i][col].figure == 1) {
                    arrXCountHor++
                    if (arrXCountHor == 3) {
                        winCol = true
                    }
                }
            }

            for (i in 0..2) {
                if (cellCoordinatersList[row][i].figure == 0) {
                    arrOCountVer++
                    if (arrOCountVer == 3) {
                        winRow = true
                    }
                }
                if (cellCoordinatersList[row][i].figure == 1) {
                    arrXCountVer++
                    if (arrXCountVer == 3) {
                        winRow = true
                    }
                }
            }

            for (i in 0..2) {
                if (cellCoordinatersList[i][i].figure == 0) {
                    arrOCountDiagLeft++
                    if (arrOCountDiagLeft == 3) {
                        winDiagLeft = true
                    }
                }
                if (cellCoordinatersList[i][i].figure == 1) {
                    arrXCountDiagRight++
                    if (arrXCountDiagRight == 3) {
                        winDiagLeft = true
                    }
                }
            }

            if (cellCoordinatersList[0][2].figure == 1 && cellCoordinatersList[1][1].figure == 1 && cellCoordinatersList[2][0].figure == 1) {
                winDiagRight = true
            } else if (cellCoordinatersList[0][2].figure == 0 && cellCoordinatersList[1][1].figure == 0 && cellCoordinatersList[2][0].figure == 0) {
                winDiagRight = true
            }

            if (winCol) {
                winLineStartX = cellCoordinatersList[0][col]._x + sideSize / 4
                winLineStartY = cellCoordinatersList[0][col]._y + sideSize / 2
                winLineEndX = cellCoordinatersList[2][col]._x + 3 * sideSize / 4
                winLineEndY = cellCoordinatersList[2][col]._y + sideSize / 2
                drawWinLine = true
                onScoreChange(lastGameCell.figure)
                postInvalidateDelayed(500)
            }

            if (winRow) {
                winLineStartX = cellCoordinatersList[row][0]._x + sideSize / 2
                winLineStartY = cellCoordinatersList[row][0]._y + sideSize / 2
                winLineEndX = cellCoordinatersList[row][2]._x + sideSize / 2
                winLineEndY = cellCoordinatersList[row][2]._y + sideSize / 2
                drawWinLine = true
                onScoreChange(lastGameCell.figure)
                postInvalidateDelayed(500)
            }

            if (winDiagLeft) {
                winLineStartX = cellCoordinatersList[0][0]._x + sideSize / 2
                winLineStartY = cellCoordinatersList[0][0]._y + sideSize / 2
                winLineEndX = cellCoordinatersList[2][2]._x + sideSize / 2
                winLineEndY = cellCoordinatersList[2][2]._y + sideSize / 2
                drawWinLine = true
                onScoreChange(lastGameCell.figure)
                postInvalidateDelayed(500)
            }

            if (winDiagRight) {
                winLineStartX = cellCoordinatersList[0][2]._x + sideSize / 2
                winLineStartY = cellCoordinatersList[0][2]._y + sideSize / 2
                winLineEndX = cellCoordinatersList[2][0]._x + sideSize / 2
                winLineEndY = cellCoordinatersList[2][0]._y + sideSize / 2
                drawWinLine = true
                onScoreChange(lastGameCell.figure)
                postInvalidateDelayed(500)
            }

            if (arrOfGameCells.size == 9) {
                onResetGameMsg()
                resetGame()
            }
        }
    }

    private fun getCellIndex(coordinate: Float): Int {
        val index: Int
        if (coordinate > 0 && (coordinate < sideSize)) {
            index = 0
        } else if (coordinate > sideSize && coordinate < sideSize * 2) {
            index = 1
        } else {
            index = 2
        }
        return index
    }

    private fun drawField(canvas: Canvas) {

        val ps = paintGrid.strokeWidth / 2

        //frame
        val framePoints = floatArrayOf(
            /*left*/0f + ps, 0f, 0f + ps, sideSize * 3,
            /*top*/0f, 0f + ps, sideSize * 3, 0f + ps,
            /*right*/sideSize * 3 - ps, 0f, sideSize * 3 - ps, sideSize * 3,
            /*bottom*/0f, sideSize * 3 - ps, sideSize * 3, sideSize * 3 - ps
        )
        canvas.drawLines(framePoints, paintGrid)

        //grid
        val gridPoints = floatArrayOf(
            /*first row*/ 0f, sideSize, sideSize * 3, sideSize,
            /*second row*/ 0f, sideSize * 2, sideSize * 3, sideSize * 2,
            /*first column*/ sideSize, 0f, sideSize, sideSize * 3,
            /*second column*/ sideSize * 2, 0f, sideSize * 2, sideSize * 3
        )
        canvas.drawLines(gridPoints, paintGrid)
    }

    private fun log(msg: String) {
        Log.i(this::class.java.simpleName, msg)
    }
}