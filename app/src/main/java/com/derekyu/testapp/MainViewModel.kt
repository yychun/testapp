package com.derekyu.testapp

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.derekyu.testapp.data.IAppDataSource
import com.derekyu.testapp.data.api.ApiHelper
import com.derekyu.testapp.data.api.AppRemoteDataSource
import com.derekyu.testapp.data.api.RetrofitBuilder
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyLoadState
import com.derekyu.testapp.data.model.MyError
import com.derekyu.testapp.data.repository.inmemory.PageKeyedPagingSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(
    private val appDataSource: IAppDataSource
) : ViewModel() {

    private val appPagingSource = PageKeyedPagingSource(appDataSource)
    private val _appPage = Pager(
        PagingConfig(
            pageSize = PageKeyedPagingSource.PAGE_SIZE,
            prefetchDistance = 1,
            enablePlaceholders = true,
            initialLoadSize = 1
        )
    ) {
        appPagingSource
    }.flow
    private val _appPageLoadState: MutableLiveData<MyLoadState<PagingData<AppInfoDTO>>> =
        MutableLiveData()
    val appPageLoadState: LiveData<MyLoadState<PagingData<AppInfoDTO>>>
        get() = _appPageLoadState

    private val _appRecommendationLoadState: MutableLiveData<MyLoadState<List<AppInfoDTO>>> =
        MutableLiveData()
    val appRecommendationLoadState: LiveData<MyLoadState<List<AppInfoDTO>>>
        get() = _appRecommendationLoadState

    init {
        _appPage.catch {
            when (it) {
                is IOException, is HttpException -> {
                    _appPageLoadState.postValue(MyLoadState.Fail(MyError.Network(it)))
                }
            }
        }

        viewModelScope.launch {
            _appPage.collectLatest {
                _appPageLoadState.postValue(MyLoadState.Success(it))
            }
        }

        fetchAppRecommendationList()
    }

    fun fetchAppRecommendationList() {
        viewModelScope.launch {
            try {
                appDataSource.retrieveTopGrossingApps(TOP_GROSSING_APP_SIZE).feed.entry.map {
                    AppInfoDTO(
                        it
                    )
                }.let {
                    _appRecommendationLoadState.postValue(MyLoadState.Success(it))
                }
            } catch (e: IOException) {
                _appRecommendationLoadState.postValue(
                    MyLoadState.Fail(
                        MyError.Network(
                            e
                        )
                    )
                )
            }
        }
    }

    fun reloadAppList() {
        _appPage.retry()
    }

    companion object {
        const val TOP_GROSSING_APP_SIZE = 10
    }
}

/**
 * Factory for [MainViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(AppRemoteDataSource(RetrofitBuilder.apiService)) as T
    }
}