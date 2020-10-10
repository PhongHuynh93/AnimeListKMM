package com.wind.animelist.androidApp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.ui.adapter.TitleViewHolder
import com.wind.animelist.androidApp.databinding.ItemCharacterListBinding
import com.wind.animelist.androidApp.databinding.ItemTitleBinding
import com.wind.animelist.androidApp.ui.adapter.vh.CharacterHozListViewHolder
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.DetailManga
import com.wind.animelist.shared.viewmodel.model.MangaCharacter
import com.wind.animelist.shared.viewmodel.model.Title

/**
 * Created by Phong Huynh on 10/8/2020
 */
class DetailMangaAdapter(private val requestManager: RequestManager) : ListAdapter<DetailManga, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<DetailManga>() {
    override fun areItemsTheSame(oldItem: DetailManga, newItem: DetailManga): Boolean {
        return oldItem.getType() == newItem.getType()
    }

    override fun areContentsTheSame(oldItem: DetailManga, newItem: DetailManga): Boolean {
        return oldItem == newItem
    }
}) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterTypeUtil.TYPE_TITLE -> {
                TitleViewHolder(
                    ItemTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            AdapterTypeUtil.TYPE_CHARACTER_LIST -> {
                val binding = ItemCharacterListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CharacterHozListViewHolder(requestManager, binding)
            }
            else -> {
                throw IllegalStateException("must create vh with itemview $viewType")
            }
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            AdapterTypeUtil.TYPE_TITLE -> {
                val vh2 = vh as TitleViewHolder
                vh2.binding.text = (item as Title).text
                vh2.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_CHARACTER_LIST -> {
                val vh2 = vh as CharacterHozListViewHolder
                vh2.binding.item = (item as MangaCharacter).list
                vh2.binding.executePendingBindings()
            }
        }
    }

    interface Callback {
        fun onClick(view: View, pos: Int, item: DetailManga)
    }
}