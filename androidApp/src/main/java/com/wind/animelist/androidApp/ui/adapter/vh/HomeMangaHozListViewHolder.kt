package com.wind.animelist.androidApp.ui.adapter.vh

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.ui.adapter.HomeMangaHozAdapter
import com.wind.animelist.androidApp.util.spaceNormal
import com.wind.animelist.androidApp.util.spacePrettySmall

/**
 * Created by Phong Huynh on 10/22/2020
 */
private const val NUMB_ROW = 2

class HomeMangaHozListViewHolder(
    context: Context,
    requestManager: RequestManager,
    itemView: View,
    callbackManga: HomeMangaHozAdapter.Callback?
) : RecyclerView.ViewHolder(itemView) {
    var rcv: RecyclerView = itemView.findViewById(R.id.rcv)

    init {
        rcv.apply {
            adapter = HomeMangaHozAdapter(requestManager)
                .apply {
                    callback = callbackManga
                }
            layoutManager = GridLayoutManager(context, NUMB_ROW, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            itemAnimator = null
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.right = context.spacePrettySmall
                    val pos = parent.getChildAdapterPosition(view)
                    if (pos % NUMB_ROW > 0) {
                        outRect.top = context.spacePrettySmall / NUMB_ROW
                    }
                    if (pos < NUMB_ROW) {
                        outRect.left = context.spaceNormal
                    }
                }
            })
        }
    }
}
