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
import com.wind.animelist.androidApp.databinding.ItemMoreInfoBinding
import com.wind.animelist.androidApp.databinding.ItemTitleBinding
import com.wind.animelist.androidApp.model.Divider
import com.wind.animelist.androidApp.model.Title
import com.wind.animelist.androidApp.ui.adapter.vh.CharacterHozListViewHolder
import com.wind.animelist.androidApp.ui.adapter.vh.MoreInfoViewHolder
import com.wind.animelist.shared.viewmodel.model.*

/**
 * Created by Phong Huynh on 10/8/2020
 */
class DetailMangaAdapter(private val requestManager: RequestManager) : ListAdapter<DetailManga, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<DetailManga>() {
    override fun areItemsTheSame(oldItem: DetailManga, newItem: DetailManga): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DetailManga, newItem: DetailManga): Boolean {
        return true
    }
}) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun getItemViewType(position: Int): Int {
        return if (position in 0 until itemCount) {
            getItem(position).getType()
        } else {
            -1
        }
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
            AdapterTypeUtil.TYPE_MORE_INFO -> {
                val binding = ItemMoreInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MoreInfoViewHolder(binding)
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
                vh2.binding.item = (item as DetailMangaCharacter).list
                vh2.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_MORE_INFO -> {
                val vh2 = vh as MoreInfoViewHolder
                (item as DetailMangaMoreInfo).let {
                    vh2.binding.title = it.title
                    vh2.binding.description = it.description
                }
                vh2.binding.executePendingBindings()
            }
        }
    }

    fun setData(list: List<DetailManga>) {
        val newList = mutableListOf<DetailManga>()
        for (item in list) {
            if (item is DetailMangaCharacter) {
                newList.add(Title(item.title))
                newList.add(item)
            } else {
                newList.add(item)
            }
        }
        submitList(newList)
    }

    interface Callback {
        fun onClick(view: View, pos: Int, item: DetailManga)
    }
}