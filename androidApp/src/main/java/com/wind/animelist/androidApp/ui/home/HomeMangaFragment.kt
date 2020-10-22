package com.wind.animelist.androidApp.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.ui.adapter.*
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.HomeMangaViewModel
import com.wind.animelist.shared.viewmodel.NavViewModel
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import util.*
import util.loadmore.LoadMoreHelper
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Phong Huynh on 9/26/2020
 */
@ExperimentalCoroutinesApi
class HomeMangaFragment : Fragment(R.layout.fragment_home) {
    companion object {
        fun newInstance(): HomeMangaFragment {
            return HomeMangaFragment()
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeMangaAdapter.apply {
            callbackManga = object: HomeMangaHozAdapter.Callback {
                override fun onClick(view: View, pos: Int, item: Manga) {
                    view.transitionName = item.id.toString()
                    vmNav.goToManga.value = Event(view to item)
                }
            }
        }
        rcv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
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
        vmHome.data.onEach { list ->
            if (list.isEmpty()) {
                rcv.gone()
                progressBar.show()
            } else {
                rcv.show()
                progressBar.gone()
                homeMangaAdapter.setData(list)
            }
            loadMoreHelper.finishLoading()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vmHome.loadState.onEach { state ->
            loadingAdapter.loadState = state
            loadMoreHelper.loadState = state
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}