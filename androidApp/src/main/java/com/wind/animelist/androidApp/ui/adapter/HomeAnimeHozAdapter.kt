package com.wind.animelist.androidApp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.databinding.ItemAnimeBinding
import com.wind.animelist.androidApp.ui.adapter.vh.AnimeItemViewHolder
import com.wind.animelist.shared.domain.model.Anime

/**
 * Created by Phong Huynh on 10/22/2020
 */

class HomeAnimeHozAdapter constructor(private val requestManager: RequestManager) :
    ListAdapter<Anime, AnimeItemViewHolder>(object : DiffUtil
    .ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeItemViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeItemViewHolder(binding).apply {
            itemView.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos >= 0) {
                    getItem(pos)?.let {
                        callback?.onClick(view, pos, it)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: AnimeItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.requestManager = requestManager
        holder.binding.executePendingBindings()
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(view: View, pos: Int, item: Anime)
    }
}