package com.wind.animelist.androidApp.ui.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleViewHolder
import com.wind.animelist.androidApp.databinding.*
import com.wind.animelist.androidApp.di.homeModule
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.viewmodel.HomeViewModel
import com.wind.animelist.shared.viewmodel.HomeViewModelFactory
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.NavViewModel
import com.wind.animelist.shared.viewmodel.di.homeVModule
import com.wind.animelist.shared.viewmodel.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.di
import org.kodein.di.instance
import util.Event
import util.TYPE_FOOTER
import util.forwardTransition
import util.getDimen
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 9/26/2020
 */
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(R.layout.fragment_home), DIAware {
    private lateinit var viewBinding: FragmentHomeBinding
    override val di: DI
        get() = subDI(parentDI = di()) {
            import(homeModule(this@HomeFragment))
        }

    val homeAdapter: HomeAdapter by instance()
    val loadingAdapter: LoadingAdapter by instance()
    val titleHeaderAdapter: TitleHeaderAdapter by instance()
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, titleHeaderAdapter, homeAdapter, loadingAdapter)
        titleHeaderAdapter.submitList(listOf(getString(R.string.home_title)))
        adapter
    }
    val vmHome by viewModels<HomeViewModel> {
        HomeViewModelFactory(subDI(di()) {
            import(homeVModule)
        })
    }
    val loadMoreHelper: LoadMoreHelper by instance()
    private val vmNav by activityViewModels<NavViewModel>()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forwardTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            vm = vmHome
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
                val spaceSmall = getDimen(R.dimen.space_small)
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
                                outRect.top = spaceNormal.toInt()
                                outRect.bottom = spaceNormal.toInt()
                            }
                        }
                        if (pos == concatAdapter.itemCount - 1) {
                            outRect.bottom = spaceNormal.toInt()
                        }
                    }
                })
                loadMoreHelper.handleLoadmore(this) {
                    Toast.makeText(
                        requireContext(),
                        "Please implement loadmore",
                        Toast.LENGTH_SHORT
                    ).show()
//                    vmHome.loadMoreManga()
                }
            }
        }
        return viewBinding.root
    }
}

@BindingAdapter("lifecycle", "data", "loadState")
fun RecyclerView.loadHome(lifecycleOwner: LifecycleOwner, data: CFlow<List<HomeItem>>?, loadState: CFlow<LoadState>?) {
    data?.onEach { list ->
        (adapter as ConcatAdapter).apply {
            adapters.forEach { adapter ->
                when (adapter) {
                    is HomeAdapter -> {
                        adapter.submitList(list)
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

class HomeAdapter constructor(
    private val applicationContext: Context,
    private val requestManager: RequestManager
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return true
    }
}) {
    private val spaceNormal = applicationContext.getDimen(R.dimen.space_normal).toInt()
    var callbackManga: HomeMangaHozAdapter.Callback? = null
    var callbackAnime: HomeAnimeHozAdapter.Callback? = null

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
            AdapterTypeUtil.TYPE_ANIME_LIST -> {
                val binding = ItemMangaHomePagerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HomeAnimeHozListViewHolder(binding)
            }
            AdapterTypeUtil.TYPE_MANGA_LIST -> {
                val binding = ItemMangaHomePagerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HomeMangaHozListViewHolder(binding)
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
            AdapterTypeUtil.TYPE_ANIME_LIST -> {
                val homePagerViewHolder = holder as HomeAnimeHozListViewHolder
                homePagerViewHolder.binding.item = item
                homePagerViewHolder.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_MANGA_LIST -> {
                val homePagerViewHolder = holder as HomeMangaHozListViewHolder
                homePagerViewHolder.binding.item = item
                homePagerViewHolder.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                val vh = holder as TitleViewHolder
                vh.binding.text = (item as Title).text
                vh.binding.executePendingBindings()
            }
        }
    }

    inner class HomeMangaHozListViewHolder(val binding: ItemMangaHomePagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rcv.apply {
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
                        outRect.right = spaceNormal
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
    inner class HomeAnimeHozListViewHolder(val binding: ItemMangaHomePagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rcv.apply {
                adapter = HomeAnimeHozAdapter(requestManager)
                    .apply {
                        callback = callbackAnime
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
                        outRect.right = spaceNormal
                        val pos = parent.getChildAdapterPosition(view)
                        if (pos == 0) {
                            outRect.left = spaceNormal
                        }
                    }
                })
            }
        }
    }
}

@BindingAdapter("data")
fun RecyclerView.loadManga(data: HomeItem?) {
    data?.let {
        when (data) {
            is HomeManga -> (adapter as HomeMangaHozAdapter).submitList(data.list)
            is HomeAnime -> (adapter as HomeAnimeHozAdapter).submitList(data.list)
        }
    }
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