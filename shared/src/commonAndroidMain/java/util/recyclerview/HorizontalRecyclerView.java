package util.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chungnh on 12/13/2019.
 */
public class HorizontalRecyclerView extends RecyclerView implements RecyclerView.OnChildAttachStateChangeListener {

    /**
     * This SparseArray is used to store the height of each child view (inside ItemView)
     * into SparseIntArray {@link #isViewHeightChanged(View, int)}
     * corresponding to each ItemViewType
     */
    @NonNull
    private final SparseArray<SparseIntArray> mChildViewHeightSparseArray = new SparseArray<>();
    @NonNull
    private final Set<Integer> mPosAttachedSet = new HashSet<>();
    private int mCurrentItemViewType;

    private int mPointId;
    private int mInitX;
    private int mInitY;

    public HorizontalRecyclerView(@NonNull Context context) {
        super(context);
    }

    public HorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);
        // Call this method at here because need use LayoutManager,
        // avoid case LayoutManager has been not set
        if (!(layout instanceof LinearLayoutManager) || ((LinearLayoutManager) layout).getOrientation() == RecyclerView.VERTICAL) {
            return;
        }
        // Disabled Measurement Cache to make sure LayoutManager will re-measure children height if needed
        layout.setMeasurementCacheEnabled(false);
        addOnChildAttachStateChangeListener(this);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        mPosAttachedSet.clear();
        mChildViewHeightSparseArray.clear();
        setMinimumHeight(0);
    }

    /**
     * @param itemView: View
     * @param viewType: int
     * @return true if child view's height is increased or view type is changed
     */
    private boolean isViewHeightChanged(@NonNull final View itemView, int viewType) {
        final List<View> children = getChildrenFromViewList(itemView);
        boolean isNeedRequestLayout = false;
        if (mCurrentItemViewType != viewType) {
            mCurrentItemViewType = viewType;
            isNeedRequestLayout = true;
        }
        // SparseIntArray contains key is id and value is corresponding height of each children in item view
        final SparseIntArray childViewHeightSparseArray = mChildViewHeightSparseArray.get(viewType, new SparseIntArray());
        for (final View view : children) {
            int viewId = view.getId();
            // ignore if view has not been set id yet
            if (viewId == NO_ID) {
                continue;
            }
            int viewHeight = getMeasuredHeight(view, itemView);
            if (childViewHeightSparseArray.indexOfKey(viewId) < 0) {
                childViewHeightSparseArray.put(viewId, viewHeight);
                continue;
            }
            if (childViewHeightSparseArray.get(viewId) < viewHeight) {
                childViewHeightSparseArray.put(viewId, viewHeight);
                isNeedRequestLayout = true;
            }
        }
        mChildViewHeightSparseArray.put(viewType, childViewHeightSparseArray);
        return isNeedRequestLayout;
    }

    private int getMeasuredHeight(@NonNull final View view, @NonNull final View parent) {
        view.measure(MeasureSpec.makeMeasureSpec(parent.getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return view.getMeasuredHeight();
    }

    @NonNull
    private List<View> getChildrenFromViewList(@NonNull final View view) {
        if (!(view instanceof ViewGroup)) {
            return Collections.emptyList();
        }
        final ViewGroup viewGroup = (ViewGroup) view;
        final List<View> result = new ArrayList<>();
        performTraversalViewGroup(viewGroup, result);
        return result;
    }

    private void performTraversalViewGroup(@NonNull final ViewGroup view, List<View> result) {
        // Add root
        result.add(view);
        final int childCount = view.getChildCount();
        for (int index = 0; index < childCount; ++index) {
            final View child = view.getChildAt(index);
            if (child instanceof ViewGroup) {
                performTraversalViewGroup((ViewGroup) child, result);
            } else {
                result.add(child);
            }
        }
    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        final Adapter adapter = getAdapter();
        final int position = getChildAdapterPosition(view);
        if (adapter == null || mPosAttachedSet.contains(position)) {
            return;
        }
        post(() -> {
            if ((isViewHeightChanged(view, adapter.getItemViewType(position)))) {
                requestLayout();
            }
            mPosAttachedSet.add(position);
        });
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int height = bottom - top;
        if (getMinimumHeight() < height) {
            setMinimumHeight(height);
        }
    }

    /**
     * HorizontalRecyclerview #onInterceptTouchEvent
     * Decide with which action should be intercept.
     * In case of scrolling horizontal just ignore and allow user scroll the horizontal list.
     * Otherwise, just intercept user's action for easy scrolling vertical parent outside.
     * Now, HorizontalRecyclerview doesn't assume any dragging vertical instead of one of horizon.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPointId = e.getPointerId(0);
                mInitX = Math.round(e.getX());
                mInitY = Math.round(e.getY());
                break;
            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mPointId);
                if (index < 0) {
                    return false;
                }
                final int x = Math.round(e.getX());
                final int y = Math.round(e.getY());
                final int dx = x - mInitX;
                final int dy = y - mInitY;
                if (getScrollState() != SCROLL_STATE_DRAGGING) {
                    if (Math.abs(dx) >= Math.abs(dy)) {
                        return super.onInterceptTouchEvent(e);
                    } else {
                        return false;
                    }
                }
            }
            break;
            default:
                return false;
        }
        return super.onInterceptTouchEvent(e);
    }
}
