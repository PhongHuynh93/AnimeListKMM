package com.wind.animelist.androidApp.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.FragmentMoreBinding
import com.wind.animelist.androidApp.databinding.ItemMangaBinding
import com.wind.animelist.androidApp.model.TitleManga
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.adapter.vh.MangaItemViewHolder
import com.wind.animelist.androidApp.viewmodel.NavViewModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.MoreMangaViewModel
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import util.*
import util.loadmore.LoadMoreHelper


private const val EXTRA_DATA = "xData"

@ExperimentalCoroutinesApi
class MoreMangaFragment : Fragment() {
    private lateinit var viewBinding: FragmentMoreBinding
    private val titleManga: TitleManga by lazy {
        requireArguments().getParcelable(EXTRA_DATA)!!
    }
    private val loadMoreHelper: LoadMoreHelper by inject { parametersOf(this) }
    private val moreAdapter: MoreAdapter by inject { parametersOf(this) }
    private val loadingAdapter: LoadingAdapter by inject { parametersOf(this) }
    private val titleHeaderAdapter: TitleHeaderAdapter by inject { parametersOf(this) }
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, moreAdapter, loadingAdapter)
        titleHeaderAdapter.submitList(listOf(titleManga.mangaList.title))
        adapter
    }

    private val vmMore by viewModels<MoreMangaViewModel>()

    private val vmNav by activityViewModels<NavViewModel>()
    var callbackDetail: MoreAdapter.Callback? = null

    companion object {
        fun newInstance(titleManga: TitleManga): MoreMangaFragment {
            return MoreMangaFragment().apply {
                arguments = bundleOf(EXTRA_DATA to titleManga)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vmMore.setInfo(titleManga.mangaList.list, titleManga.mangaList.loadMoreInfo, titleManga.mangaList.page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMoreBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            moreAdapter.apply {
                callbackDetail = object : MoreAdapter.Callback {
                    override fun onClick(pos: Int, item: Manga) {
                        vmNav.goToManga.value = Event(item)
                    }
                }
                moreAdapter.setCallbackDetail(callbackDetail as MoreAdapter.Callback)
            }
            setUpToolbar(toolbar, titleManga.mangaList.title, showUpIcon = true)
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 3)
                    .apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return if (concatAdapter.getItemViewType(position) == TYPE_FOOTER) {
                                    3
                                } else {
                                    1
                                }
                            }
                        }
                    }
                adapter = concatAdapter
                val spacing = getDimen(R.dimen.space_normal).toInt()
                val spaceSmall = getDimen(R.dimen.space_small).toInt()
                val spanCount = 3
                val includeEdge = true

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
                            TYPE_FOOTER -> {
                                outRect.top = spacing
                                outRect.bottom = spacing
                            }

                            // TODO: 10/30/2020 make it in another class
                            AdapterTypeUtil.TYPE_MANGA_GRID -> {
                                val column: Int = posBinding % spanCount // item column

                                if (includeEdge) {
                                    outRect.left =
                                        spaceSmall - column * spaceSmall / spanCount // spaceSmall - column * ((1f / spanCount) * spaceSmall)
                                    outRect.right =
                                        (column + 1) * spaceSmall / spanCount // (column + 1) * ((1f / spanCount) * spaceSmall)
                                    if (posBinding < spanCount) { // top edge
                                        outRect.top = spaceSmall
                                    }
                                    outRect.bottom = spaceSmall // item bottom
                                } else {
                                    outRect.left =
                                        column * spaceSmall / spanCount // column * ((1f / spanCount) * spaceSmall)
                                    outRect.right =
                                        spaceSmall - (column + 1) * spaceSmall / spanCount // spaceSmall - (column + 1) * ((1f /    spanCount) * spaceSmall)
                                    if (posBinding >= spanCount) {
                                        outRect.top = spaceSmall // item top
                                    }
                                }
                            }
                        }
                    }
                })
                loadMoreHelper.setVisibleThreshold(spanCount * 3)
                loadMoreHelper.handleLoadmore(this) {
                    vmMore.loadMore()
                }
            }
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "setInfo: ${titleManga.mangaList.list}")
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vmMore.data.onEach { list ->
                Log.e("TAG", "onViewCreated: $list")
                if (list.isEmpty()) {
                    viewBinding.rcv.gone()
                    viewBinding.progressBar.show()
                } else {
                    viewBinding.rcv.show()
                    viewBinding.progressBar.gone()
                    moreAdapter.setData(list)
                }
            }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vmMore.loadState.onEach { state ->
                loadingAdapter.loadState = state
            }.collect()
        }
    }
}

class MoreAdapter constructor(
    private val applicationContext: Context,
    private val requestManager: RequestManager
) : ListAdapter<Manga, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<Manga>() {
    override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem == newItem
    }
}) {
    var callback: Callback? = null

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int {
        return AdapterTypeUtil.TYPE_MANGA_GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaItemViewHolder {
        val binding = ItemMangaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MangaItemViewHolder(binding).apply {
            itemView.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos >= 0) {
                    getItem(pos)?.let {
                        callback?.onClick(pos, it)
                    }
                }
            }
        }
    }

    fun setCallbackDetail(cb: Callback) {
        callback = cb
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val vh = holder as MangaItemViewHolder
        vh.binding.item = item
        holder.binding.requestManager = requestManager
        holder.binding.executePendingBindings()
    }

    fun setData(data: List<Manga>) {
        submitList(data)
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(pos: Int, item: Manga)
    }
}