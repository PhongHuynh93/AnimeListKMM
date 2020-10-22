package com.wind.animelist.androidApp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.wind.animelist.androidApp.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Phong Huynh on 10/10/2020
 */
@ExperimentalCoroutinesApi
class MainFragment: Fragment(R.layout.fragment_main) {
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vPager.setUserInputEnabled(false)
        vPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeMangaFragment()
                    1 -> HomeAnimeFragment()
                    // FIXME: 10/22/2020 add the else case
                    else -> Fragment()
                }
            }
        }

        vPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_manga -> {
                    vPager.setCurrentItem(0, true)
                }
                R.id.action_anime -> {
                    vPager.setCurrentItem(1, true)
                }
                R.id.action_user -> {
                    vPager.setCurrentItem(2, true)
                }
            }
            true
        }
    }
}