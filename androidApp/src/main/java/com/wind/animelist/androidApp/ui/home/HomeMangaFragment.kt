package com.wind.animelist.androidApp.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.FragmentHomeBinding
import com.wind.animelist.androidApp.model.TitleManga
import com.wind.animelist.androidApp.ui.adapter.HomeMangaAdapter
import com.wind.animelist.androidApp.ui.adapter.HomeMangaHozAdapter
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.viewmodel.NavViewModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.HomeMangaViewModel
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import util.*
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 9/26/2020
 */
// TODO: 11/3/2020 keep the current position of the hoz manga
@ExperimentalCoroutinesApi
class HomeMangaFragment : Fragment() {
    companion object {
        fun newInstance(): HomeMangaFragment {
            return HomeMangaFragment()
        }
    }

    private lateinit var viewBinding: FragmentHomeBinding
    private val homeMangaAdapter: HomeMangaAdapter by inject { parametersOf(this) }
    private val loadingAdapter: LoadingAdapter by inject { parametersOf(this) }
    private val titleHeaderAdapter: TitleHeaderAdapter by inject { parametersOf(this) }
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, titleHeaderAdapter, homeMangaAdapter, loadingAdapter)
        titleHeaderAdapter.submitList(listOf(getString(R.string.home_title_manga)))
        adapter
    }
    private val vmHome by viewModel<HomeMangaViewModel>()
    private val loadMoreHelper: LoadMoreHelper by inject { parametersOf(this) }
    private val vmNav by activityViewModels<NavViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMangaAdapter.apply {
            callbackManga = object: HomeMangaHozAdapter.Callback {
                override fun onClick(view: View, pos: Int, item: Manga) {
                    view.transitionName = item.id.toString()
                    vmNav.goToManga.value = Event(item)
                }
            }
            callback = object: HomeMangaAdapter.Callback {
                override fun onClickMore(list: TitleManga) {
                    vmNav.goToMoreManga.value = Event(list)
                }
            }
        }
        loadingAdapter.setCallback(object: LoadingAdapter.Callback {
            override fun retry() {
                vmHome.retry()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.swipeRefresh.apply {
            val arrayOfInt = IntArray(1)
            arrayOfInt[0] = getColorAttr(R.attr.colorAccent)
            setColorSchemeColors(*arrayOfInt)
            setOnRefreshListener {
                vmHome.refresh()
            }
        }
        viewBinding.rcv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
            itemAnimator = null
            val spaceNormal = getDimen(R.dimen.space_normal)
            val spaceLarge = getDimen(R.dimen.space_large)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val pos = parent.getChildAdapterPosition(view)
                    val posBinding = parent.getChildViewHolder(view).bindingAdapterPosition
                    when (concatAdapter.getItemViewType(pos)) {
                        AdapterTypeUtil.TYPE_DIVIDER -> if (posBinding > 0) {
                            outRect.top = spaceNormal.toInt()
                        }
                        TYPE_FOOTER -> {
                            outRect.top = spaceLarge.toInt()
                            outRect.bottom = spaceLarge.toInt()
                        }
                        AdapterTypeUtil.TYPE_TITLE -> {
                            outRect.left = spaceNormal.toInt()
                        }
                    }
                    if (pos == concatAdapter.itemCount - 1) {
                        outRect.bottom = spaceNormal.toInt()
                    }
                }
            })
            // 3 for one section (divider + title + hoz list)
            loadMoreHelper.setVisibleThreshold(3)
            loadMoreHelper.handleLoadmore(this) {
                vmHome.loadMore()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vmHome.data.onEach { list ->
                Timber.e("list ${list.size} ${list.hashCode()}")
                if (list.isEmpty()) {
                    viewBinding.rcv.gone()
                    viewBinding.progressBar.show()
                } else {
                    viewBinding.rcv.show()
                    viewBinding.progressBar.gone()
                    homeMangaAdapter.setData(list)
                }
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vmHome.loadState.onEach { state ->
                // TODO: 11/3/2020 handle error view at the center
                Timber.e("loadstate $state")
                loadingAdapter.loadState = state
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vmHome.loadStateRefresh.onEach { state ->
                viewBinding.swipeRefresh.isRefreshing = state == LoadState.Loading
            }.collect()
        }
    }
}