package com.derekyu.testapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyLoadState
import com.derekyu.testapp.ui.AppsAdapter
import com.derekyu.testapp.ui.AppsLoadStateAdapter
import com.derekyu.testapp.ui.MyStateView
import com.derekyu.testapp.utils.mapToMyError
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.flow.collectLatest
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
        app_page_state_view.dataView = app_list

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
                     when AppPageMergedPagingSource.load() is called and querying is performed,
                     a LoadResult.Error is returned to avoid fetching from remote when performing query.
                     An error msg would be shown in footer section which is unintended. Thus the footer is temp removed.
                     */
                    app_list.adapter = appsAdapter
                }
            }
        }

        viewModel.appPageLoadState.observe(viewLifecycleOwner) {
            // TODO: check isEmptyData
            setAppPageState(it, false)
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

        appsAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) {
            when (val refreshLoadState = it.source.refresh) {
                is LoadState.Error -> setAppPageState(
                    MyLoadState.Error(refreshLoadState.error.mapToMyError()),
                    true
                )
                is LoadState.Loading -> setAppPageState(MyLoadState.Loading(), false)
                is LoadState.NotLoading -> {
                    lifecycleScope.launch {
                        viewModel.appPage.collectLatest {
                            setAppPageState(MyLoadState.Success(it), false)
                        }
                    }
                }
            }
        }
    }

    private fun setAppPageState(state: MyLoadState<PagingData<AppInfoDTO>>, isEmptyData: Boolean) {
        if (state is MyLoadState.Success) {
            lifecycleScope.launch {
                appsAdapter.submitData(state.data)
            }
        }
        (app_page_state_view as MyStateView<PagingData<AppInfoDTO>>).setState(state, isEmptyData)
    }
}