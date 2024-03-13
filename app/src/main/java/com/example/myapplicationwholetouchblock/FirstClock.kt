package com.example.myapplicationwholetouchblock

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FirstClock(context: Context?, attr: AttributeSet? = null) : View(context, attr) {

    private val drawableContainer = Rect()
    private val boundaryOfText = Rect()
    private val text = "Hello World"
    private val text1 = "56"
    private var middleX = 0f
    private var middleY = 0f
    private var gradientMatrix = Matrix()
    private var rotationAnimator = ValueAnimator.ofFloat(0f, 1f)
    private var gradient: SweepGradient? = null

    var colors = intArrayOf(Color.GREEN,Color.MAGENTA,Color.WHITE,Color.CYAN,Color.RED,Color.YELLOW,Color.MAGENTA,Color.BLUE,Color.CYAN,Color.RED,Color.YELLOW)
    var positions = floatArrayOf(0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f)

    init {
        rotationAnimator.apply {
            addUpdateListener {
                gradientMatrix.postRotate(-ROTATION_ANGLE, middleX, middleY)
                gradient?.setLocalMatrix(gradientMatrix)
                invalidate()
            }
            duration = ANIMATION_DURATION_MS
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            drawableContainer[0, 0, w] = h
            val fontSize =  calculateFontSize(boundaryOfText, drawableContainer, text)
            digitalClockPaintAM.textSize=fontSize
            digitalClockPaint.textSize=fontSize
            TextPaint.textSize=fontSize

            gradient  = SweepGradient(width/2f, height / 2f, colors, positions).apply {
                setLocalMatrix(gradientMatrix)
            }
            circlePaintBackground.shader = gradient
            TextPaint.shader=gradient
            rotationAnimator.start()
        }
        invalidate()
    }

    companion object {
        private const val ROTATION_ANGLE = 5f
        private const val ANIMATION_DURATION_MS = 1000L

        private fun calculateFontSize(textBounds: Rect, textContainer: Rect, text: String): Float {
            val textPaint = Paint()
            var stage = 1
            var textSize = 0f
            while (stage < 3) {
                if (stage == 1) textSize += 10f else if (stage == 2) textSize -= 1f
                textPaint.textSize = textSize
                textPaint.getTextBounds(text, 0, text.length, textBounds)
                textBounds.offsetTo(textContainer.left, textContainer.top)
                val fits = textContainer.contains(textBounds)
                if (stage == 1 && !fits) stage++ else if (stage == 2 && fits) stage++
            }
            return textSize
        }
    }

    override fun onDetachedFromWindow() {
        rotationAnimator.cancel()
        super.onDetachedFromWindow()
    }

    val digitalClockPaintAM= Paint().apply {
        color = Color.LTGRAY
        textSize = 30f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    val digitalClockPaint = Paint().apply {
        color = Color.MAGENTA
        textSize = 210f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    val LinePaint1 = Paint().apply {
        color = Color.LTGRAY
        strokeWidth=5f
        isFakeBoldText = true
    }

    val TextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 30f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    val circlePaint = Paint().apply {
        color = Color.LTGRAY
    }

    val circlePaintBackground = Paint().apply {
        color = Color.LTGRAY
        style=Paint.Style.STROKE
        strokeWidth=25f
    }

    val BatteryTextPaint = Paint().apply {
        color = Color.GREEN
        style=Paint.Style.STROKE
        strokeWidth=10f
    }

    val BatteryTextPaint1 = Paint().apply {
        color = Color.GREEN
    }

    val BatteryTextPaint11 = Paint().apply {
        color = Color.WHITE
        textSize = 10f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        val radius=width.coerceAtLeast(width/2)/2.2f  //2.5
        canvas.drawCircle(width / 2f, height / 2f, radius, circlePaintBackground)

        canvas.clipRect(0, 0, width, height)
        val halfTextHeight = boundaryOfText.height() / 2f

        val currentTimeAM = SimpleDateFormat("aa", Locale.getDefault()).format(Date())
        canvas.drawText(currentTimeAM, drawableContainer.centerX().toFloat(), drawableContainer.centerY()/1.6f + halfTextHeight, digitalClockPaintAM)

        val currentTimeHour = SimpleDateFormat("HH", Locale.getDefault()).format(Date())
        canvas.drawText(currentTimeHour, drawableContainer.centerX().toFloat(), drawableContainer.centerY()/1.1f + halfTextHeight, digitalClockPaint)

        val currentTimeMinutes = SimpleDateFormat("mm", Locale.getDefault()).format(Date())
        canvas.drawText(currentTimeMinutes, drawableContainer.centerX().toFloat(), drawableContainer.centerY()/0.8f + halfTextHeight, digitalClockPaint)

        canvas.drawCircle(width / 5f, height / 2f, radius/20, circlePaint)
        canvas.drawCircle(width / 4f, height / 2f, radius/20, circlePaint)
        canvas.drawCircle(width / 3.3f, height / 2f, radius/20, circlePaint)

        canvas.drawCircle(width / 5*3.5f, height / 2f, radius/20, circlePaint)
        canvas.drawCircle(width / 5*3.75f, height / 2f, radius/20, circlePaint)
        canvas.drawCircle(width / 5*4f, height / 2f, radius/20, circlePaint)

        val calender = Calendar.getInstance().time
        val dateFormate=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calender)  //0.58
        canvas.drawText(dateFormate,drawableContainer.centerX().toFloat(), drawableContainer.centerY()/0.54f + halfTextHeight,TextPaint)

        canvas.drawLine(width / 1.64f,height / 1.9f,width / 2.5f,height / 1.9f,LinePaint1)

        val rectF = RectF(width/3.1f, height/2.2f, width/3.6f, height/2.45f)
        canvas.drawRoundRect(rectF, 8f, 8f, BatteryTextPaint)

        val rectF1 = RectF(width/3.1f, height/2.34f, width/3.6f, height/2.18f)
        canvas.drawRoundRect(rectF1, 8f, 8f, BatteryTextPaint1)

        //canvas.drawText(text1,drawableContainer.centerX().toFloat(), drawableContainer.centerY()/0.54f + halfTextHeight,BatteryTextPaint11)

    }
}

