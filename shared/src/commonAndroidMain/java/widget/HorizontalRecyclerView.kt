package widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 */
class HorizontalRecyclerView : RecyclerView, OnChildAttachStateChangeListener {
    /**
     * This SparseArray is used to store the height of each child view (inside ItemView)
     * into SparseIntArray [.isViewHeightChanged]
     * corresponding to each ItemViewType
     */
    private val mChildViewHeightSparseArray = SparseArray<SparseIntArray>()
    private val mPosAttachedSet: MutableSet<Int> = HashSet()
    private var mCurrentItemViewType = 0
    private var mPointId = 0
    private var mInitX = 0
    private var mInitY = 0

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        // Call this method at here because need use LayoutManager,
        // avoid case LayoutManager has been not set
        if (layout !is LinearLayoutManager || layout.orientation == VERTICAL) {
            return
        }
        // Disabled Measurement Cache to make sure LayoutManager will re-measure children height if needed
        layout.setMeasurementCacheEnabled(false)
        addOnChildAttachStateChangeListener(this)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        mPosAttachedSet.clear()
        mChildViewHeightSparseArray.clear()
        minimumHeight = 0
    }

    /**
     * @param itemView: View
     * @param viewType: int
     * @return true if child view's height is increased or view type is changed
     */
    private fun isViewHeightChanged(itemView: View, viewType: Int): Boolean {
        val children = getChildrenFromViewList(itemView)
        var isNeedRequestLayout = false
        if (mCurrentItemViewType != viewType) {
            mCurrentItemViewType = viewType
            isNeedRequestLayout = true
        }
        // SparseIntArray contains key is id and value is corresponding height of each children in item view
        val childViewHeightSparseArray = mChildViewHeightSparseArray[viewType, SparseIntArray()]
        for (view in children) {
            val viewId = view.id
            // ignore if view has not been set id yet
            if (viewId.toLong() == NO_ID) {
                continue
            }
            val viewHeight = getMeasuredHeight(view, itemView)
            if (childViewHeightSparseArray.indexOfKey(viewId) < 0) {
                childViewHeightSparseArray.put(viewId, viewHeight)
                continue
            }
            if (childViewHeightSparseArray[viewId] < viewHeight) {
                childViewHeightSparseArray.put(viewId, viewHeight)
                isNeedRequestLayout = true
            }
        }
        mChildViewHeightSparseArray.put(viewType, childViewHeightSparseArray)
        return isNeedRequestLayout
    }

    private fun getMeasuredHeight(view: View, parent: View): Int {
        view.measure(
            MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        return view.measuredHeight
    }

    private fun getChildrenFromViewList(view: View): List<View> {
        if (view !is ViewGroup) {
            return emptyList()
        }
        val result: MutableList<View> = ArrayList()
        performTraversalViewGroup(view, result)
        return result
    }

    private fun performTraversalViewGroup(view: ViewGroup, result: MutableList<View>) {
        // Add root
        result.add(view)
        val childCount = view.childCount
        for (index in 0 until childCount) {
            val child = view.getChildAt(index)
            if (child is ViewGroup) {
                performTraversalViewGroup(child, result)
            } else {
                result.add(child)
            }
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        val adapter = adapter
        val position = getChildAdapterPosition(view)
        if (adapter == null || mPosAttachedSet.contains(position)) {
            return
        }
        post {
            if (isViewHeightChanged(view, adapter.getItemViewType(position))) {
                requestLayout()
            }
            mPosAttachedSet.add(position)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {}
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val height = bottom - top
        if (minimumHeight < height) {
            minimumHeight = height
        }
    }

    /**
     * HorizontalRecyclerview #onInterceptTouchEvent
     * Decide with which action should be intercept.
     * In case of scrolling horizontal just ignore and allow user scroll the horizontal list.
     * Otherwise, just intercept user's action for easy scrolling vertical parent outside.
     * Now, HorizontalRecyclerview doesn't assume any dragging vertical instead of one of horizon.
     */
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPointId = e.getPointerId(0)
                mInitX = e.x.roundToInt()
                mInitY = e.y.roundToInt()
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
                        super.onInterceptTouchEvent(e)
                    } else {
                        false
                    }
                }
            }
            else -> return false
        }
        return super.onInterceptTouchEvent(e)
    }
}