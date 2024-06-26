package com.example.pedestriantrafficlightdetection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.round

class RectView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var results: ArrayList<Result>? = null
    private lateinit var classes: Array<String>

    private val textPaint = Paint().also {
        it.textSize = 60f
        it.color = Color.WHITE
    }

    fun transformRect(results: ArrayList<Result>) {
        // scale 구하기
        val scaleY = height / DataProcess.INPUT_SIZE.toFloat()
        val scaleX = scaleY * 9f / 16f
        val realX = height * 9f / 16f
        val diffX = realX - width

        results.forEach {
            it.rectF.left = it.rectF.left * scaleX - (diffX / 2f)
            it.rectF.right = it.rectF.right * scaleX - (diffX / 2f)
            it.rectF.top *= scaleY
            it.rectF.bottom *= scaleY
        }
        this.results = results
    }

    override fun onDraw(canvas: Canvas) {
        //그림 그리기
        results?.forEach {
            canvas?.drawRect(it.rectF, findPaint(it.classIndex))
            canvas?.drawText(
                classes[it.classIndex] + ", " + round(it.score * 100) + "%",
                it.rectF.left + 10,
                it.rectF.top + 60,
                textPaint
            )
        }
        super.onDraw(canvas)
    }

    fun setClassLabel(classes: Array<String>) {
        this.classes = classes
    }

    //paint 지정
    private fun findPaint(classIndex: Int): Paint {
        val paint = Paint()
        paint.style = Paint.Style.STROKE    // 빈 사각형 그림
        paint.strokeWidth = 10.0f           // 굵기 10
        paint.strokeCap = Paint.Cap.ROUND   // 끝을 뭉특하게
        paint.strokeJoin = Paint.Join.ROUND // 끝 주위도 뭉특하게
        paint.strokeMiter = 100f            // 뭉특한 정도는 100도

        //임의로 지정한 색상
        paint.color = when (classIndex) {
            0 -> Color.GREEN
            1 -> Color.RED
            else -> Color.DKGRAY
        }
        return paint
    }
}