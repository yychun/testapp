package com.derekyu.testapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.derekyu.testapp.data.model.MyLoadState
import com.derekyu.testapp.ui.AppsAdapter
import com.derekyu.testapp.ui.AppsLoadStateAdapter
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

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        appsAdapter = AppsAdapter()
        app_list.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        app_list.adapter = appsAdapter.withLoadStateFooter(
            footer = AppsLoadStateAdapter(appsAdapter)
        )
        app_list_button_retry.setOnClickListener {
            viewModel.reloadAppList()
        }

        viewModel.appPageLoadState.observe(viewLifecycleOwner) {
            when (it) {
                is MyLoadState.Success -> {
                    lifecycleScope.launch {
                        app_list_button_retry.visibility = View.GONE
                        app_list.visibility = View.VISIBLE

                        appsAdapter.submitData(it.list)
                    }
                }
                is MyLoadState.Fail -> {
                    app_list_button_retry.visibility = View.VISIBLE
                    app_list_button_retry.text =
                        "${getString(R.string.retry)} (${it.error.errorMsg(requireContext())})"
                    app_list.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.appRecommendationLoadState.observe(viewLifecycleOwner) {
            when (it) {
                is MyLoadState.Success -> {
                    app_recommendation_view.submitData(it.list)
                }
                is MyLoadState.Fail -> {
                    app_recommendation_view.showRetry(it.error)
                }
            }
        }
        app_recommendation_view.retryCallback = {
            viewModel.fetchAppRecommendationList()
        }
    }
}