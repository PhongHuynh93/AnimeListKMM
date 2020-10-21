package com.wind.animelist.androidApp.ui.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.databinding.FragmentHomeBinding
import com.wind.animelist.androidApp.databinding.ItemAnimeBinding
import com.wind.animelist.androidApp.databinding.ItemMangaBinding
import com.wind.animelist.androidApp.databinding.ItemTitleBinding
import com.wind.animelist.androidApp.model.Divider
import com.wind.animelist.androidApp.model.Title
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleViewHolder
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.viewmodel.HomeViewModel
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.NavViewModel
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.Home
import com.wind.animelist.shared.viewmodel.model.MangaList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import util.*
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 9/26/2020
 */
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding
    val homeAdapter: HomeAdapter by inject { parametersOf(this) }
    val loadingAdapter: LoadingAdapter by inject { parametersOf(this) }
    val titleHeaderAdapter: TitleHeaderAdapter by inject { parametersOf(this) }
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, titleHeaderAdapter, homeAdapter, loadingAdapter)
        titleHeaderAdapter.submitList(listOf(getString(R.string.home_title)))
        adapter
    }
    val vmHome by viewModel<HomeViewModel>()
    val loadMoreHelper: LoadMoreHelper by inject { parametersOf(this) }
    private val vmNav by activityViewModels<NavViewModel>()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            homeAdapter.apply {
                callbackAnime = object: HomeAnimeHozAdapter.Callback {
                    override fun onClick(view: View, pos: Int, item: Anime) {
                        vmNav.goToAnime.value = Event(item)
                    }
                }
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
                    vmHome.loadMoreManga()
                }
            }
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmHome.data.onEach { list ->
            if (list.isEmpty()) {
                viewBinding.rcv.gone()
                viewBinding.progressBar.show()
            } else {
                viewBinding.rcv.show()
                viewBinding.progressBar.gone()
                homeAdapter.setData(list)
            }
            loadMoreHelper.finishLoading()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        vmHome.loadState.onEach { state ->
            loadingAdapter.loadState = state
            loadMoreHelper.loadState = state
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}

class HomeAdapter constructor(
    private val applicationContext: Context,
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
    private val spaceNormal = applicationContext.getDimen(R.dimen.space_normal).toInt()
    private val spaceSmall = applicationContext.getDimen(R.dimen.space_small).toInt()
    var callbackManga: HomeMangaHozAdapter.Callback? = null
    var callbackAnime: HomeAnimeHozAdapter.Callback? = null
    private val inflater = LayoutInflater.from(applicationContext)

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
            AdapterTypeUtil.TYPE_MANGA_LIST -> {
                HomeMangaHozListViewHolder(inflater.inflate(R.layout.item_manga_home_pager, parent, false))
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
            AdapterTypeUtil.TYPE_MANGA_LIST -> {
                val homePagerViewHolder = holder as HomeMangaHozListViewHolder
                (homePagerViewHolder.rcv.adapter as HomeMangaHozAdapter).submitList((item as MangaList).list)
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                val vh = holder as TitleViewHolder
                vh.binding.text = (item as Title).text
                vh.binding.executePendingBindings()
            }
        }
    }

    fun setData(list: List<Home>) {
        val newHomeList = mutableListOf<Home>()
        for (item in list) {
            if (item is MangaList) {
                newHomeList.add(Divider)
                newHomeList.add(Title(item.title))
                newHomeList.add(item)
            } else {
                newHomeList.add(item)
            }
        }
        submitList(newHomeList)
    }

    inner class HomeMangaHozListViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rcv = itemView.findViewById<RecyclerView>(R.id.rcv)
        init {
            rcv.apply {
                adapter = HomeMangaHozAdapter(requestManager)
                    .apply {
                        callback = callbackManga
                    }
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                itemAnimator = null
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.right = spaceSmall
                        val pos = parent.getChildAdapterPosition(view)
                        if (pos == 0) {
                            outRect.left = spaceNormal
                        }
                    }
                })
            }
        }
    }

    inner class DividerViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView)
}

class HomeMangaHozAdapter constructor(private val requestManager: RequestManager) :
    ListAdapter<Manga, HomeMangaHozAdapter.ViewHolder>(object : DiffUtil
    .ItemCallback<Manga>() {
        override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem == newItem
        }
    }) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMangaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos >= 0) {
                    getItem(pos)?.let {
                        callback?.onClick(view, pos, it)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.requestManager = requestManager
        holder.binding.executePendingBindings()
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(view: View, pos: Int, item: Manga)
    }

    inner class ViewHolder(val binding: ItemMangaBinding) : RecyclerView.ViewHolder(binding.root)
}

class HomeAnimeHozAdapter constructor(private val requestManager: RequestManager) :
    ListAdapter<Anime, HomeAnimeHozAdapter.ViewHolder>(object : DiffUtil
    .ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener { view ->
                val pos = bindingAdapterPosition
                if (pos >= 0) {
                    getItem(pos)?.let {
                        callback?.onClick(view, pos, it)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.requestManager = requestManager
        holder.binding.executePendingBindings()
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(view: View, pos: Int, item: Anime)
    }

    inner class ViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root)
}