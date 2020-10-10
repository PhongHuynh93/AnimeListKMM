package com.wind.animelist.androidApp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.databinding.ItemDetailMangaHeaderBinding
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.DetailManga
import com.wind.animelist.shared.viewmodel.model.DetailMangaHeader
import java.lang.IllegalStateException

/**
 * Created by Phong Huynh on 10/8/2020
 */
class DetailMangaAdapter(private val requestManager: RequestManager) : ListAdapter<DetailManga, DetailMangaAdapter.ViewHolder>(object : DiffUtil
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            AdapterTypeUtil.TYPE_DETAIL_MANGA_HEADER -> {
                val binding = ItemDetailMangaHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(
                    binding
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
            else -> {
                throw IllegalStateException("must create vh with itemview $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            AdapterTypeUtil.TYPE_DETAIL_MANGA_HEADER -> {
                holder.binding.item = (item as DetailMangaHeader).manga
                holder.binding.executePendingBindings()
            }
        }

    }

    interface Callback {
        fun onClick(view: View, pos: Int, item: DetailManga)
    }

    inner class ViewHolder(val binding: ItemDetailMangaHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)
}