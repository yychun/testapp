package com.derekyu.testapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.derekyu.testapp.ui.AppsAdapter
import com.derekyu.testapp.ui.AppsLoadStateAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

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
        initAdapter()
    }

    private fun initAdapter() {
        appsAdapter = AppsAdapter()
        app_list.adapter = appsAdapter.withLoadStateFooter(
            footer = AppsLoadStateAdapter(appsAdapter)
        )

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            viewModel.appPage.collectLatest {
                appsAdapter.submitData(it)
            }
            viewModel.appRecommendationList.observe(viewLifecycleOwner) {
                app_recommendation_view.submitData(it)
            }
        }
    }
}