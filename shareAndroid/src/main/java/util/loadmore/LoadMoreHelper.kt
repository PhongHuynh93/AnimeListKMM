package util.loadmore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Phong Huynh on 10/6/2020
 */
class LoadMoreHelper(private val fragmentManager: FragmentManager) {
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
        frag.get().callback = callback
        frag.get().handleLoadmore(rcv)
    }

    fun setLoading(loading: Boolean) {
        frag.get().loading = loading
    }

    fun setVisibleThreshold(visibleThreshold: Int) {
        frag.get().visibleThreshold = visibleThreshold
    }
}

class LoadMoreHelperFragment : Fragment() {
    var loading = false
    var visibleThreshold = 5
    lateinit var callback: () -> Unit

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

    fun handleLoadmore(rcv: RecyclerView) {
        if (rcv.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = rcv.layoutManager as LinearLayoutManager
            rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        callback.invoke()
                        loading = true;
                    }
                }
            })
        }
    }
}

@FunctionalInterface
interface OnLoadMoreListener {
    fun onLoadMore()
}