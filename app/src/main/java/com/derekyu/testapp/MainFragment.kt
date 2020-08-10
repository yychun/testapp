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
import androidx.recyclerview.widget.DividerItemDecoration
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
    private lateinit var appsAdapter: AppsAdapter

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
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.startQuery(newText)
                return true
            }
        })

        appsAdapter = AppsAdapter()
        app_list.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        app_list.adapter = appsAdapter.withLoadStateFooter(
            footer = AppsLoadStateAdapter(appsAdapter)
        )

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
            viewModel.reloadAppList()
        }
    }
}