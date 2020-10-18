package com.wind.animelist.androidApp.ui.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.FragmentDetailMangaBinding
import com.wind.animelist.androidApp.ui.adapter.CharacterAdapter
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.shared.domain.model.Character
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.viewmodel.DetailMangaViewModel
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.DetailManga
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import util.TYPE_FOOTER
import util.getDimen
import util.setUpToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Phong Huynh on 10/8/2020
 */
private const val EXTRA_DATA = "xData"
private const val EXTRA_TRANSITION_NAME = "xTransitionName"

@ExperimentalCoroutinesApi
class DetailMangaFragment() : Fragment() {
    private lateinit var manga: Manga
    private lateinit var viewBinding: FragmentDetailMangaBinding
    val vmDetailManga by viewModel<DetailMangaViewModel>()
    val detailMangaAdapter: DetailMangaAdapter by inject { parametersOf(this) }
    val loadingAdapter: LoadingAdapter by inject { parametersOf(this) }
    val detailMangaHeaderAdapter: DetailMangaHeaderAdapter by inject { parametersOf(this) }
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, detailMangaHeaderAdapter, detailMangaAdapter, loadingAdapter)
        adapter
    }

    companion object {
        fun newInstance(manga: Manga, transitionName: String): DetailMangaFragment {
            return DetailMangaFragment().apply {
                arguments = bundleOf(EXTRA_DATA to manga, EXTRA_TRANSITION_NAME to transitionName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manga = requireArguments()[EXTRA_DATA] as Manga
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetailMangaBinding.inflate(inflater, container, false).apply {
            vm = vmDetailManga
            lifecycleOwner = viewLifecycleOwner
            requestManager = Glide.with(this@DetailMangaFragment)
            item = manga
            detailMangaHeaderAdapter.submitList(listOf(manga))
            setUpToolbar(toolbar, showUpIcon = true)
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = concatAdapter
                val space = getDimen(R.dimen.space_normal)
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)

                        parent.adapter!!.getItemViewType(parent.getChildAdapterPosition(view))
                            .let { type ->
                                when (type) {
                                    AdapterTypeUtil.TYPE_TITLE -> {
                                        outRect.top = space.toInt()
                                    }
                                    TYPE_FOOTER -> {
                                        outRect.top = space.toInt()
                                        outRect.bottom = space.toInt()
                                    }
                                    AdapterTypeUtil.TYPE_MORE_INFO -> {
                                        outRect.left = space.toInt()
                                        outRect.right = space.toInt()
                                        outRect.top = space.toInt()
                                    }
                                }
                            }
                        if (parent.getChildAdapterPosition(view) == adapter!!.itemCount - 1) {
                            outRect.bottom = space.toInt() * 2
                        }
                    }
                })
            }
        }
        return viewBinding.root.apply {
            transitionName = requireArguments()[EXTRA_TRANSITION_NAME] as String
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmDetailManga.setManga(manga)
    }
}

@BindingAdapter("lifecycle", "data", "loadState")
fun RecyclerView.loadDetailManga(
    lifecycleOwner: LifecycleOwner,
    data: CFlow<List<DetailManga>>?,
    loadState: CFlow<LoadState>?
) {
    data?.onEach { list ->
        (adapter as ConcatAdapter).apply {
            adapters.forEach { adapter ->
                when (adapter) {
                    is DetailMangaAdapter -> {
                        adapter.setData(list)
                    }
                }
            }
        }
    }?.launchIn(lifecycleOwner.lifecycleScope)

    loadState?.onEach { state ->
        (adapter as ConcatAdapter).adapters.forEach { adapter ->
            when (adapter) {
                is LoadingAdapter -> {
                    adapter.loadState = state
                }
            }
        }
    }?.launchIn(lifecycleOwner.lifecycleScope)
}

@BindingAdapter("data")
fun RecyclerView.loadCharacterList(list: List<Character>?) {
    list?.let {
        (adapter as CharacterAdapter).submitList(it)
    }
}