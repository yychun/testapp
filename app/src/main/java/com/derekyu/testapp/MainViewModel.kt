package com.derekyu.testapp

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.derekyu.testapp.data.api.ApiHelper
import com.derekyu.testapp.data.api.RetrofitBuilder
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.repository.inmemory.PageKeyedPagingSource
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiHelper: ApiHelper
) : ViewModel() {
    val appPage = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = true
        )
    ) {
        PageKeyedPagingSource(apiHelper)
    }.flow

    init {
        fetchAppRecommendationList()
    }

    private val _appRecommendationList: MutableLiveData<List<AppInfoDTO>> = MutableLiveData()
    val appRecommendationList: LiveData<List<AppInfoDTO>>
        get() = _appRecommendationList

    private fun fetchAppRecommendationList() {
        viewModelScope.launch {
            _appRecommendationList.postValue(apiHelper.retrieveTopGrossingApps(10).feed.entry.map {
                AppInfoDTO(
                    it
                )
            })
        }
    }
}

/**
 * Factory for [MainViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {
    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(apiHelper) as T
    }
}