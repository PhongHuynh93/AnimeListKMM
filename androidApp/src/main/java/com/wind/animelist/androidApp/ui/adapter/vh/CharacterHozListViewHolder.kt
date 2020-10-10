package com.wind.animelist.androidApp.ui.adapter.vh

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.ItemCharacterListBinding
import com.wind.animelist.androidApp.ui.adapter.CharacterAdapter
import util.getDimen

/**
 * Created by Phong Huynh on 10/10/2020
 */
class CharacterHozListViewHolder(val requestManager: RequestManager, val binding: ItemCharacterListBinding): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.rcv.apply {
            val spaceHoz = itemView.context.getDimen(R.dimen.space_normal)
            adapter = CharacterAdapter(requestManager)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.right = spaceHoz.toInt()
                    parent.getChildAdapterPosition(view).let {pos ->
                        if (pos == 0) {
                            outRect.left = spaceHoz.toInt()
                        }
                    }
                }
            })
        }
    }
}

