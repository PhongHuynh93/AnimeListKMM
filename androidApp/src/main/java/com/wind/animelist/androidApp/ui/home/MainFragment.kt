package com.wind.animelist.androidApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.FragmentMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Phong Huynh on 10/10/2020
 */
@ExperimentalCoroutinesApi
class MainFragment: Fragment() {
    private lateinit var viewBinding: FragmentMainBinding

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.vPager.isUserInputEnabled = false
        viewBinding.vPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeMangaFragment.newInstance()
                    1 -> HomeAnimeFragment()
                    else -> TestFragment()
                }
            }
        }

        viewBinding.vPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBinding.bottomNav.menu.getItem(position).isChecked = true
            }
        })

        viewBinding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_manga -> {
                    viewBinding.vPager.setCurrentItem(0, true)
                }
                R.id.action_anime -> {
                    viewBinding.vPager.setCurrentItem(1, true)
                }
                R.id.action_user -> {
                    viewBinding.vPager.setCurrentItem(2, true)
                }
            }
            true
        }
    }
}