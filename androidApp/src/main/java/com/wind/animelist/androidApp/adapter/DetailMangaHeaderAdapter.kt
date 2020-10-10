package com.wind.animelist.androidApp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.databinding.ItemDetailMangaHeaderBinding
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil

/**
 * Created by Phong Huynh on 10/8/2020
 */
class DetailMangaHeaderAdapter(private val requestManager: RequestManager) :
    ListAdapter<Manga, DetailMangaHeaderAdapter.ViewHolder>(object : DiffUtil
    .ItemCallback<Manga>() {
        override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem == newItem
        }
    }) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun getItemViewType(position: Int): Int {
        return AdapterTypeUtil.TYPE_DETAIL_MANGA_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDetailMangaHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding.apply {
                requestManager = this@DetailMangaHeaderAdapter.requestManager
            }
        ).apply {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.executePendingBindings()
    }

    interface Callback {
        fun onClick(view: View, pos: Int, item: Manga)
    }

    inner class ViewHolder(val binding: ItemDetailMangaHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)
}