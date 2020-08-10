package com.derekyu.testapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyLoadState
import com.derekyu.testapp.ui.AppsAdapter
import com.derekyu.testapp.ui.AppsLoadStateAdapter
import com.derekyu.testapp.ui.MyStateView
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels { LiveDataVMFactory }
    private val appsAdapter: AppsAdapter by lazy { AppsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.startQuery(newText)
                return true
            }
        })

        app_list.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        app_list.adapter = appsAdapter.withLoadStateFooter(
            footer = AppsLoadStateAdapter(appsAdapter)
        )
        app_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)) {
                    if (viewModel.shouldRetryLoadingAppPage) {
                        viewModel.onRetryAppPage()
                        appsAdapter.retry()
                    }
                }
            }
        })
        viewModel.isQuerying.observe(viewLifecycleOwner) {
            val hasFooter = app_list.adapter is ConcatAdapter
            if (!it) {
                if (!hasFooter) {
                    // restore footer which is removed before
                    app_list.adapter = appsAdapter.withLoadStateFooter(
                        footer = AppsLoadStateAdapter(appsAdapter)
                    )
                }
            } else {
                if (hasFooter) {
                    /*
                     when AppPageMergedPagingSource.load() is called and querying is performed, a LoadResult.Error is returned,
                     in order not to fetch from remote.
                     since an error msg would be shown in footer section, the footer is temp removed
                     */
                    app_list.adapter = appsAdapter
                }
            }
        }

        viewModel.appPageLoadState.observe(viewLifecycleOwner) {
            if (it is MyLoadState.Success) {
                lifecycleScope.launch {
                    appsAdapter.submitData(it.data)
                }
            }
            (app_page_state_view as MyStateView<PagingData<AppInfoDTO>>).apply {
                dataView = app_list
                // TODO: check isEmptyData
                setState(it, false)
            }
        }
        viewModel.appRecommendationLoadState.observe(viewLifecycleOwner) {
            app_recommendation_view.setState(it)
            if (it is MyLoadState.Success) {
                app_recommendation_view.submitData(it.data)
            }
        }
        app_recommendation_view.onRetryCallback = {
            viewModel.fetchAppRecommendationList()
        }
        app_page_state_view.onRetryCallback = {
            viewModel.onRetryAppPage()
            appsAdapter.retry()
        }
    }
}