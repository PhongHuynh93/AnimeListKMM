package widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.wind.collagePhotoMaker.share.R

class RatioImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {
    // width/height
    private var mRatio = 0f

    init {
        val typedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.RatioImageView, 0, defStyleAttr)
        try {
            val ratio = typedArray.getString(R.styleable.RatioImageView_riv_dimensionRatio)
            if (!TextUtils.isEmpty(ratio)) {
                val wh = ratio!!.split(":").toTypedArray()
                if (wh.size == 2) {
                    mRatio = wh[0].toFloat() / wh[1].toFloat()
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mRatio > 0) {
            val (width, height) = measure(widthMeasureSpec, heightMeasureSpec, mRatio.toDouble())
            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * @param ratio: height/width
     */
    fun setRatio(ratio: Float) {
        mRatio = ratio
        requestLayout()
    }

}