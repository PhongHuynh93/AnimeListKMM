package widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 *
 * VerticalRecyclerView is used to scrolling vertical screen.
 * For custom handling nested child which is mostly horizontal child like #HorizontalRecyclerView.
 * Basic flow nested scroll is: Scrollable Child View(RecyclerView) -> Scrollable Parent View(RecyclerView, ViewPager) -> ...
 * Intercept flow is: parentView -> VerticalRecyclerView -> ChildView(RecyclerView,...) -> ...
 */
class VerticalRecyclerView : RecyclerView {
    private var mPointId = 0
    private var mInitX = 0
    private var mInitY = 0

    companion object {
        fun isScrollable(recyclerView: RecyclerView?): Boolean {
            if (recyclerView == null) {
                return false
            }
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val adapter = recyclerView.adapter
            if (layoutManager == null || adapter == null) {
                return false
            }
            val spanCount = if (layoutManager is GridLayoutManager) layoutManager.spanCount else 1
            return !(layoutManager.findLastCompletelyVisibleItemPosition() == (adapter.itemCount - 1) / spanCount
                    && layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
        }
    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        /*
         * This is an exception. we don't need to create a custom CoordinatorLayout for all screen to handle this action.
         * Means the case:
         * CoordinatorLayout(No need custom due to the validation of below one) -> VerticalRecyclerView -> HorizontalRecyclerView
         */
        if (target is RecyclerView
            && target.layoutManager != null && target.layoutManager!!.canScrollHorizontally()
            && isScrollable(target)
            && nestedScrollAxes == SCROLL_AXIS_HORIZONTAL && parent is CoordinatorLayout
        ) {
            if (target.isNestedScrollingEnabled()) {
                target.setNestedScrollingEnabled(false)
            }
            return true
        }
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    /**
     * Decide which cases have nested scroll. So Vertical RecyclerView mostly has horizontal scrolling child.
     * So now, avoid abusing call #setNestedScrollingEnabled(false); That could ignore nested scrolling.
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (abs(dx) >= abs(dy)) {
            super.onNestedPreScroll(target, dx, dy, consumed)
        }
    }

    /**
     * Vertical RecyclerView #onInterceptTouchEvent
     * Decide with which action should be intercept.
     * Ignore vertical scroll of Vertical RecyclerView while there is occurring of dragging horizontal -> for easy scroll hoz.
     * Otherwise, allow user scroll vertical without hesitation
     */
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPointId = e.getPointerId(0)
                mInitX = e.x.roundToInt()
                mInitY = e.y.roundToInt()
                if (scrollState == SCROLL_STATE_SETTLING) {
                    stopScroll()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val index = e.findPointerIndex(mPointId)
                if (index < 0) {
                    return false
                }
                val x = e.x.roundToInt()
                val y = e.y.roundToInt()
                val dx = x - mInitX
                val dy = y - mInitY
                if (scrollState != SCROLL_STATE_DRAGGING) {
                    return if (abs(dx) >= abs(dy)) {
                        false
                    } else {
                        super.onInterceptTouchEvent(e)
                    }
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}