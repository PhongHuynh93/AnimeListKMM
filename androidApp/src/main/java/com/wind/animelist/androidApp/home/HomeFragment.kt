package com.wind.animelist.androidApp.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.adapter.TitleViewHolder
import com.wind.animelist.androidApp.databinding.*
import com.wind.animelist.androidApp.di.homeModule
import com.wind.animelist.androidApp.model.HomeAnime
import com.wind.animelist.androidApp.model.HomeItem
import com.wind.animelist.androidApp.model.HomeManga
import com.wind.animelist.androidApp.model.Title
import com.wind.animelist.androidApp.util.AdapterTypeUtil
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import org.kodein.di.*
import org.kodein.di.android.subDI
import org.kodein.di.android.x.di
import util.getDimen

/**
 * Created by Phong Huynh on 9/26/2020
 */
class HomeFragment : Fragment(R.layout.fragment_home), DIAware {
    private lateinit var viewBinding: FragmentHomeBinding
    override val di: DI
        get() = subDI(parentDI = di()) {
            import(homeModule(this@HomeFragment))
        }

    val homeAdapter: HomeAdapter by instance()
    val vmHome by viewModels<HomeViewModel>()

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
            vm = vmHome
            lifecycleOwner = viewLifecycleOwner
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = homeAdapter
            }
        }
        return viewBinding.root
    }
}

@BindingAdapter("data")
fun RecyclerView.loadData(data: List<HomeItem>?) {
    data?.let {
        (adapter as HomeAdapter).setData(it)
    }
}

class HomeAdapter constructor(private val applicationContext: Context, private val homeMangaHozAdapter: HomeMangaHozAdapter, private val homeAnimeHozAdapter: HomeAnimeHozAdapter) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return true
    }
}) {
    private val spaceNormal = applicationContext.getDimen(R.dimen.space_normal).toInt()

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterTypeUtil.TYPE_ANIME_SLIDER -> {
                val binding = ItemMangaHomePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomeAnimeHozListViewHolder(binding)
            }
            AdapterTypeUtil.TYPE_MANGA_SLIDER -> {
                val binding = ItemMangaHomePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomeMangaHozListViewHolder(binding)
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                TitleViewHolder(ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                throw IllegalStateException("Not support viewType $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            AdapterTypeUtil.TYPE_ANIME_SLIDER -> {
                val homePagerViewHolder = holder as HomeAnimeHozListViewHolder
                homePagerViewHolder.binding.item = item
                homePagerViewHolder.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_MANGA_SLIDER -> {
                val homePagerViewHolder = holder as HomeMangaHozListViewHolder
                homePagerViewHolder.binding.item = item
                homePagerViewHolder.binding.executePendingBindings()
            }
            AdapterTypeUtil.TYPE_TITLE -> {
                val vh = holder as TitleViewHolder
                vh.binding.text = applicationContext.getString((item as Title).resId)
            }
        }
    }

    fun setData(data: List<HomeItem>) {
        submitList(data)
    }

    inner class HomeMangaHozListViewHolder(val binding: ItemMangaHomePagerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rcv.apply {
                adapter = homeMangaHozAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                addItemDecoration(object: RecyclerView.ItemDecoration() {
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
    inner class HomeAnimeHozListViewHolder(val binding: ItemMangaHomePagerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rcv.apply {
                adapter = homeAnimeHozAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
                addItemDecoration(object: RecyclerView.ItemDecoration() {
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

class HomeMangaHozAdapter constructor(private val requestManager: RequestManager): ListAdapter<Manga, HomeMangaHozAdapter.ViewHolder>(object : DiffUtil
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

class HomeAnimeHozAdapter constructor(private val requestManager: RequestManager): ListAdapter<Anime, HomeAnimeHozAdapter.ViewHolder>(object : DiffUtil
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