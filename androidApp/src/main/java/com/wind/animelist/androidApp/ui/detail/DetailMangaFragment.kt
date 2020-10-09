package com.wind.animelist.androidApp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.wind.animelist.androidApp.adapter.DetailMangaHeaderAdapter
import com.wind.animelist.androidApp.adapter.LoadingAdapter
import com.wind.animelist.androidApp.databinding.FragmentDetailMangaBinding
import com.wind.animelist.androidApp.di.detailMangaModule
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.DetailMangaViewModel
import com.wind.animelist.shared.viewmodel.DetailMangaViewModelFactory
import com.wind.animelist.shared.viewmodel.di.detailMangaVModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.di
import org.kodein.di.instance

/**
 * Created by Phong Huynh on 10/8/2020
 */
private const val EXTRA_DATA = "xData"
class DetailMangaFragment(): Fragment(), DIAware {
    private lateinit var manga: Manga
    private lateinit var viewBinding: FragmentDetailMangaBinding
    val vmDetailManga by viewModels<DetailMangaViewModel> {
        DetailMangaViewModelFactory(subDI(di()) {
            import(detailMangaVModule)
        })
    }
    override val di: DI
        get() = subDI(parentDI = di()) {
            import(detailMangaModule(this@DetailMangaFragment))
        }
    val detailMangaAdapter: DetailMangaAdapter by instance()
    val detailMangaHeaderAdapter: DetailMangaHeaderAdapter by instance()
    val loadingAdapter: LoadingAdapter by instance()
    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val adapter = ConcatAdapter(config, detailMangaHeaderAdapter, detailMangaAdapter, loadingAdapter)
        adapter
    }


    companion object {
        fun newInstance(manga: Manga): DetailMangaFragment {
            return DetailMangaFragment().apply {
                arguments = bundleOf(EXTRA_DATA to manga)
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
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = concatAdapter
            }
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailMangaHeaderAdapter.submitList(listOf(manga))
    }
}