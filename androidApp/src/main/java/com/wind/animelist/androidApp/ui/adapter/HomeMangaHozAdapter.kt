package com.wind.animelist.androidApp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.databinding.ItemMangaBinding
import com.wind.animelist.androidApp.ui.adapter.vh.MangaItemViewHolder
import com.wind.animelist.shared.domain.model.Manga

/**
 * Created by Phong Huynh on 10/22/2020
 */
class HomeMangaHozAdapter constructor(private val requestManager: RequestManager) : RecyclerView.Adapter<MangaItemViewHolder>() {

    private val data = mutableListOf<Manga>()

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaItemViewHolder {
        val binding = ItemMangaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MangaItemViewHolder(binding).apply {
            itemView.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos >= 0) {
                    data[pos].let {
                        callback?.onClick(view, pos, it)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MangaItemViewHolder, position: Int) {
        holder.binding.item = data[position]
        holder.binding.requestManager = requestManager
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = data.size

    fun setData(data: List<Manga>) {
        this.data.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(view: View, pos: Int, item: Manga)
    }


}