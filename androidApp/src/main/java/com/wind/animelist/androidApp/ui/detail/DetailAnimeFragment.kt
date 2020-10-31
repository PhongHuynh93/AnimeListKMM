package com.wind.animelist.androidApp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wind.animelist.androidApp.databinding.FragmentDetailAnimeBinding
import com.wind.animelist.shared.domain.model.Anime

/**
 * Created by Phong Huynh on 10/31/2020
 */
class DetailAnimeFragment: Fragment() {
    private lateinit var viewBinding: FragmentDetailAnimeBinding

    companion object {
        fun newInstance(anime: Anime): DetailAnimeFragment {
            return DetailAnimeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetailAnimeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }
}