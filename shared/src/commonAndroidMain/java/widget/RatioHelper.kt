package widget

import android.view.View.MeasureSpec

/**
 * Created by Phong Huynh on 9/30/2020
 */
/**
 * Measure with a specific aspect ratio<br></br>
 * <br></br>
 *
 * @param widthMeasureSpec  The width <tt>MeasureSpec</tt> passed in your <tt>View.onMeasure()</tt> method
 * @param heightMeasureSpec The height <tt>MeasureSpec</tt> passed in your <tt>View.onMeasure()</tt> method
 * @param aspectRatio       The aspect ratio to calculate measurements in respect to
 */
fun measure(widthMeasureSpec: Int, heightMeasureSpec: Int, aspectRatio: Double): Pair<Int, Int> {
    val widthMode = MeasureSpec.getMode(widthMeasureSpec)
    val widthSize =
        if (widthMode == MeasureSpec.UNSPECIFIED) Int.MAX_VALUE else MeasureSpec.getSize(
            widthMeasureSpec
        )
    val heightMode = MeasureSpec.getMode(heightMeasureSpec)
    val heightSize =
        if (heightMode == MeasureSpec.UNSPECIFIED) Int.MAX_VALUE else MeasureSpec.getSize(
            heightMeasureSpec
        )
    val measuredWidth: Int
    val measuredHeight: Int
    if (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 1: Both width and height fixed
         */
        measuredWidth = widthSize
        measuredHeight = heightSize
    } else if (heightMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 2: Width dynamic, height fixed
         */
        measuredWidth = widthSize.toDouble().coerceAtMost(heightSize * aspectRatio).toInt()
        measuredHeight = (measuredWidth / aspectRatio).toInt()
    } else if (widthMode == MeasureSpec.EXACTLY) {
        /*
         * Possibility 3: Width fixed, height dynamic
         */
        measuredHeight = heightSize.toDouble().coerceAtMost(widthSize / aspectRatio).toInt()
        measuredWidth = (measuredHeight * aspectRatio).toInt()
    } else {
        /*
         * Possibility 4: Both width and height dynamic
         */
        if (widthSize > heightSize * aspectRatio) {
            measuredHeight = heightSize
            measuredWidth = (measuredHeight * aspectRatio).toInt()
        } else {
            measuredWidth = widthSize
            measuredHeight = (measuredWidth / aspectRatio).toInt()
        }
    }
    return Pair(measuredWidth, measuredHeight)
}
