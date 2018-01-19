package tech.picnic.fingerpaintview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import nl.picnic.fingerpaintview.R

class FingerPaintImageView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0,
                                                     defStyleRes: Int = 0) :
        AppCompatImageView(context, attrs, defStyleAttr) {

    private val defaultStrokeColor = Color.WHITE
    private val defaultStrokeWidth = 12f
    private val defaultTouchTolerance = 4f

    var inEditMode = false

    var strokeColor = defaultStrokeColor
        set(value) {
            field = value
            pathPaint.color = value
        }

    var strokeWidth = defaultStrokeWidth
        set(value) {
            field = value
            pathPaint.strokeWidth = value
        }

    private val matrixValues = FloatArray(9)
        get() = field.apply { imageMatrix.getValues(this) }

    var touchTolerance = defaultTouchTolerance

    private val pathPaint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
        it.color = strokeColor
        it.style = Paint.Style.STROKE
        it.strokeJoin = Paint.Join.ROUND
        it.strokeCap = Paint.Cap.ROUND
        it.strokeWidth = strokeWidth
    }

    private var currentX = 0f
    private var currentY = 0f
    private var paths: MutableList<Pair<Path, Paint>> = mutableListOf()

    init {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.FingerPaintImageView, defStyleAttr, defStyleRes)
            try {
                strokeColor = typedArray.getColor(R.styleable.FingerPaintImageView_strokeColor, defaultStrokeColor)
                strokeWidth = typedArray.getDimension(R.styleable.FingerPaintImageView_strokeWidth, defaultStrokeWidth)
                inEditMode = typedArray.getBoolean(R.styleable.FingerPaintImageView_inEditMode, false)
                touchTolerance = typedArray.getFloat(R.styleable.FingerPaintImageView_touchTolerance, defaultTouchTolerance)
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * If there are any paths drawn on top of the image, this will return a bitmap with the original
     * content plus the drawings on top of it. Otherwise, the original bitmap will be returned.
     */
    override fun getDrawable(): Drawable? {
        return super.getDrawable()?.let {
            if (!isModified()) return it

            val inverse = Matrix().apply { imageMatrix.invert(this) }
            val scale = FloatArray(9).apply { inverse.getValues(this) }[Matrix.MSCALE_X]

            // draw original bitmap
            val result = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            it.draw(canvas)

            val transformedPath = Path()
            val transformedPaint = Paint()
            paths.forEach { (path, paint) ->
                path.transform(inverse, transformedPath)
                transformedPaint.set(paint)
                transformedPaint.strokeWidth *= scale
                canvas.drawPath(transformedPath, transformedPaint)
            }
            BitmapDrawable(resources, result)
        }
    }

    private fun getCurrentPath() = paths.lastOrNull()?.first

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (inEditMode) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    handleTouchStart(event)
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    handleTouchMove(event)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    handleTouchEnd()
                    invalidate()
                }
            }
        }
        return true
    }

    private fun handleTouchStart(event: MotionEvent) {
        val sourceBitmap = super.getDrawable() ?: return

        val xTranslation = matrixValues[Matrix.MTRANS_X]
        val yTranslation = matrixValues[Matrix.MTRANS_Y]
        val scale = matrixValues[Matrix.MSCALE_X]

        val imageBounds = RectF(
                xTranslation,
                yTranslation,
                xTranslation + sourceBitmap.intrinsicWidth * scale,
                yTranslation + sourceBitmap.intrinsicHeight * scale)

        // make sure drawings are kept within the image bounds
        if (imageBounds.contains(event.x, event.y)) {
            paths.add(Path().also { it.moveTo(event.x, event.y) } to Paint(pathPaint))
            currentX = event.x
            currentY = event.y
        }
    }

    private fun handleTouchMove(event: MotionEvent) {
        val sourceBitmap = super.getDrawable() ?: return

        val xTranslation = matrixValues[Matrix.MTRANS_X]
        val yTranslation = matrixValues[Matrix.MTRANS_Y]
        val scale = matrixValues[Matrix.MSCALE_X]

        val xPos = event.x.coerceIn(xTranslation, xTranslation + sourceBitmap.intrinsicWidth * scale)
        val yPos = event.y.coerceIn(yTranslation, yTranslation + sourceBitmap.intrinsicHeight * scale)

        val dx = Math.abs(xPos - currentX)
        val dy = Math.abs(yPos - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            getCurrentPath()?.quadTo(currentX, currentY, (xPos + currentX) / 2, (yPos + currentY) / 2)
            currentX = xPos
            currentY = yPos
        }
    }

    private fun handleTouchEnd() = getCurrentPath()?.lineTo(currentX, currentY)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paths.forEach { canvas?.drawPath(it.first, it.second) }
    }

    /**
     * Removes the last full path from the view.
     */
    fun undo() {
        paths.takeIf { it.isNotEmpty() }?.removeAt(paths.lastIndex)
        invalidate()
    }

    /**
     * Returns true if any paths are currently drawn on the image, false otherwise.
     */
    fun isModified(): Boolean {
        return if (paths != null) {
            paths.isNotEmpty()
        } else {
            false
        }
    }

    /**
     * Clears all existing paths from the image.
     */
    fun clear() {
        paths.clear()
        invalidate()
    }
}