package tech.picnic.fingerpaintview.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_sample.*
import nl.picnic.fingerpaintingview.R

class SampleActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        close.setOnClickListener(this)
        save.setOnClickListener(this)
        undo.setOnClickListener(this)
        clear.setOnClickListener(this)
        red.setOnSeekBarChangeListener(this)
        green.setOnSeekBarChangeListener(this)
        blue.setOnSeekBarChangeListener(this)
        tolerance.setOnSeekBarChangeListener(this)
        width.setOnSeekBarChangeListener(this)
        normal.setOnClickListener(this)
        emboss.setOnClickListener(this)
        blur.setOnClickListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar?.id == red.id || seekBar?.id == green.id || seekBar?.id == blue.id) {
            val r = red.progress
            val g = green.progress
            val b = blue.progress
            val color = Color.argb(255, r, g, b)
            finger.strokeColor = color
            colorPreview.setBackgroundColor(color)
        } else if (seekBar?.id == tolerance.id) {
            finger.touchTolerance = progress.toFloat()
        } else if (seekBar?.id == width.id) {
            finger.strokeWidth = progress.toFloat()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            undo -> finger.undo()
            clear -> finger.clear()
            close -> hidePreview()
            save -> showPreview()
            emboss -> finger.emboss()
            blur -> finger.blur()
            normal -> finger.normal()
        }
    }

    private fun showPreview() {
        previewContainer.visibility = View.VISIBLE
        preview.setImageDrawable(finger.drawable)
    }

    private fun hidePreview() {
        previewContainer.visibility = View.INVISIBLE
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onBackPressed() {
        if (previewContainer.visibility == View.VISIBLE) {
            hidePreview()
        } else {
            super.onBackPressed()
        }
    }
}
