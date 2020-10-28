package com.wind.animelist.androidApp.home

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
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.adapter.BaseGridViewHolder
import com.wind.animelist.androidApp.databinding.FragmentMoreBinding
import com.wind.animelist.androidApp.databinding.ItemBaseGridBinding
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.shared.domain.mapper.parseSubType
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.BaseModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.viewmodel.MoreViewModel
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.NavViewModel
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import util.Event
import util.TYPE_FOOTER
import util.getDimen
import util.loadmore.LoadMoreHelper
import util.setUpToolbar

class MoreFragment : Fragment() {
    private lateinit var viewBinding: FragmentMoreBinding

    private val loadMoreHelper: LoadMoreHelper by inject { parametersOf(this) }
    private val moreAdapter: MoreAdapter by inject { parametersOf(this) }
    private val loadingAdapter: LoadingAdapter by inject { parametersOf(this) }
    private val titleHeaderAdapter: TitleHeaderAdapter by inject { parametersOf(this) }
    private var title: String = ""
    private var _type: String = ""
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, moreAdapter, loadingAdapter)
        titleHeaderAdapter.submitList(listOf(title))
        adapter
    }
    val vmMore by viewModels<MoreViewModel>()

    private val vmNav by activityViewModels<NavViewModel>()
    var callbackDetail: MoreAdapter.Callback? = null

    companion object {
        fun newInstance(title: String): MoreFragment {
            val fragment = MoreFragment()
            fragment.title = title
            fragment._type = parseSubType(title) as String
            return fragment;
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMoreBinding.inflate(inflater, container, false).apply {
            vm = vmMore
            lifecycleOwner = viewLifecycleOwner
            moreAdapter.apply {
                callbackDetail = object : MoreAdapter.Callback {
                    override fun onClick(view: View, pos: Int, item: BaseModel) {
                        if (item is Manga) {
                            view.transitionName = item.id.toString()
                            vmNav.goToManga.value = Event(view to item)
                        } else if (item is Anime) {
                            vmNav.goToAnime.value = Event(item)
                        }
                    }
                }
                moreAdapter.setCallbackDetail(callbackDetail as MoreAdapter.Callback)
            }
            setUpToolbar(toolbar, title, showUpIcon = true)
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(), 3)
                    .apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return if (position == 0) {
                                    1
                                } else {
                                    1
                                }

                            }

                        }
                    }
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

                            AdapterTypeUtil.TYPE_ANIME_GRID -> {
                                when {
                                    (posBinding + 1) % 3 == 1 -> {
                                        outRect.left = spaceNormal.toInt()
                                        outRect.right = spaceSmall.toInt()
                                        outRect.bottom = spaceSmall.toInt()
                                    }
                                    (posBinding + 1) % 3 == 2 -> {
                                        outRect.right = spaceSmall.toInt()
                                        outRect.bottom = spaceSmall.toInt()
                                    }
                                    (posBinding + 1) % 3 == 0 -> {
                                        outRect.right = spaceNormal.toInt()
                                        outRect.bottom = spaceSmall.toInt()
                                    }
                                }
                            }
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
fun RecyclerView.loadMore(
    lifecycleOwner: LifecycleOwner,
    data: CFlow<List<BaseModel>>?,
    loadState: CFlow<LoadState>?
) {
    data?.let {
        it.watch {
            (adapter as ConcatAdapter).apply {
                adapters.forEach { adapter ->
                    when (adapter) {
                        is MoreAdapter -> {
                            adapter.setData(it)
                        }
                    }
                }
            }
        }
    }

    loadState?.onEach { state ->
        (adapter as ConcatAdapter).adapters.forEach { adapter ->
            when (adapter) {
                is LoadingAdapter -> {
                    adapter.loadState = state
                }
            }
        }
    }
}

class MoreAdapter constructor(
    private val applicationContext: Context,
    private val requestManager: RequestManager
) : ListAdapter<BaseModel, RecyclerView.ViewHolder>(object : DiffUtil
.ItemCallback<BaseModel>() {
    override fun areItemsTheSame(oldItem: BaseModel, newItem: BaseModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BaseModel, newItem: BaseModel): Boolean {
        return true
    }
}) {
    private val spaceNormal = applicationContext.getDimen(R.dimen.space_normal).toInt()
    private lateinit var onItemClickListener: View.OnClickListener
    var callback: Callback? = null

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    @FunctionalInterface
    interface Callback {
        fun onClick(view: View, pos: Int, item: BaseModel)
    }

    override fun getItemViewType(position: Int): Int {
        return AdapterTypeUtil.TYPE_ANIME_GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterTypeUtil.TYPE_ANIME_GRID -> {
                val animeBinding =
                    ItemBaseGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BaseGridViewHolder(animeBinding)
            }
            else -> {
                throw IllegalStateException("Not support viewType $viewType")
            }
        }.apply {
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

    fun setOnItemClickListener(itemClickLitesner: View.OnClickListener) {
        onItemClickListener = itemClickLitesner
    }

    fun setCallbackDetail(cb: Callback) {
        callback = cb
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (getItemViewType(position)) {
            AdapterTypeUtil.TYPE_ANIME_GRID -> {
                val vh = holder as BaseGridViewHolder
                vh.binding.item = item
                holder.binding.requestManager = requestManager
                holder.binding.executePendingBindings()
            }
        }
    }

    fun setData(data: List<BaseModel>) {
        submitList(data)
    }

}