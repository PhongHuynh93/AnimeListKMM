package util.loadmore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.animelist.shared.viewmodel.LoadState
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete

/**
 * Created by Phong Huynh on 10/6/2020
 */
class LoadMoreHelper(private val fragmentManager: FragmentManager) {
    var loadState: LoadState = LoadState.Loading
        set(value) {
            Log.e("load", "load state $value")
            field = value
            if (value == Complete) {
                frag.get().removeLoadMore()
            } else {
                frag.get().loading = false
            }
        }
    private var frag: util.Lazy<LoadMoreHelperFragment>
    private val tag: String = LoadMoreHelper::class.java.simpleName

    init {
        frag = getLazySingleton(fragmentManager)
    }

    private fun getLazySingleton(fragmentManager: FragmentManager): util.Lazy<LoadMoreHelperFragment> {
        return object : util.Lazy<LoadMoreHelperFragment> {
            private lateinit var frag: LoadMoreHelperFragment

            override fun get(): LoadMoreHelperFragment {
                if (!::frag.isInitialized) {
                    frag = getFragment(fragmentManager)
                }
                return frag
            }
        }
    }

    private fun getFragment(fragmentManager: FragmentManager): LoadMoreHelperFragment {
        var fragment = fragmentManager.findFragmentByTag(tag)
        val isNewInstance = fragment == null
        if (isNewInstance) {
            fragment = LoadMoreHelperFragment.newInstance()
            fragmentManager.commitNow(true) {
                add(fragment, tag)
            }
        }
        return fragment as LoadMoreHelperFragment
    }

    fun handleLoadmore(rcv: RecyclerView, callback: () -> Unit) {
        frag.get().handleLoadMore(rcv, callback)
    }

    fun setVisibleThreshold(visibleThreshold: Int) {
        frag.get().visibleThreshold = visibleThreshold
    }

    fun finishLoading() {
        frag.get().loading = false
    }
}

class LoadMoreHelperFragment : Fragment() {
    private var rcv: RecyclerView? = null
    var loading = false
    var visibleThreshold = 5

    companion object {
        fun newInstance(): LoadMoreHelperFragment {
            return LoadMoreHelperFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    fun handleLoadMore(rcv: RecyclerView, callback: () -> Unit) {
        this.rcv = rcv
        if (rcv.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = rcv.layoutManager as LinearLayoutManager
            rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        callback.invoke()
                        loading = true
                    }
                }
            })
        }
    }

    fun removeLoadMore() {
        rcv?.clearOnScrollListeners()
    }
}