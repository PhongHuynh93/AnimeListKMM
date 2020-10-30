package com.wind.animelist.androidApp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.ItemTitleBinding
import com.wind.animelist.androidApp.model.Divider
import com.wind.animelist.androidApp.model.TitleAnime
import com.wind.animelist.androidApp.ui.adapter.vh.DividerViewHolder
import com.wind.animelist.androidApp.ui.adapter.vh.HomeAnimeHozListViewHolder
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.AnimeList
import com.wind.animelist.shared.viewmodel.model.Home
import util.inflater

/**
 * Created by Phong Huynh on 10/22/2020
 */

class HomeAnimeAdapter constructor(
    private val context: Context,
    private val requestManager: RequestManager
) : ListAdapter<Home, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<Home>() {
    override fun areItemsTheSame(oldItem: Home, newItem: Home): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Home, newItem: Home): Boolean {
        return true
    }
}) {
    var callbackAnime: HomeAnimeHozAdapter.Callback? = null

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterTypeUtil.TYPE_DIVIDER -> {
                return DividerViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_divider, parent, false)
                )
            }
            AdapterTypeUtil.TYPE_ANIME_LIST -> {
                HomeAnimeHozListViewHolder(context, requestManager, context.inflater.inflate(R.layout.hoz_recyclerview, parent, false), callbackAnime)
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                TitleViewHolder(
                    ItemTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw IllegalStateException("Not support viewType $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            AdapterTypeUtil.TYPE_ANIME_LIST -> {
                val homePagerViewHolder = holder as HomeAnimeHozListViewHolder
                (homePagerViewHolder.rcv.adapter as HomeAnimeHozAdapter).submitList((item as AnimeList).list)
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                val vh = holder as TitleViewHolder
                vh.binding.text = (item as TitleAnime).text
                vh.binding.executePendingBindings()
            }
        }
    }

    fun setData(list: List<Home>) {
        val newHomeList = mutableListOf<Home>()
        for (item in list) {
            if (item is AnimeList) {
                newHomeList.add(Divider)
                newHomeList.add(TitleAnime(item.title, item.list))
                newHomeList.add(item)
            } else {
                newHomeList.add(item)
            }
        }
        submitList(newHomeList)
    }
}