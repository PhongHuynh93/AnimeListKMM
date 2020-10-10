package com.wind.animelist.androidApp.ui.adapter.vh

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.databinding.ItemCharacterListBinding
import com.wind.animelist.androidApp.ui.adapter.CharacterAdapter

/**
 * Created by Phong Huynh on 10/10/2020
 */
class CharacterHozListViewHolder(val requestManager: RequestManager, val binding: ItemCharacterListBinding): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.rcv.apply {
            adapter = CharacterAdapter(requestManager)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }
}

