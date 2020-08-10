package com.derekyu.testapp

import androidx.lifecycle.*
import androidx.paging.*
import com.derekyu.testapp.data.AppPageLocalRepository
import com.derekyu.testapp.data.remote.AppRemoteDataSource
import com.derekyu.testapp.data.remote.RetrofitBuilder
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.MyLoadState
import com.derekyu.testapp.data.model.MyError
import com.derekyu.testapp.data.pagingsource.local.AppPageMergedPagingSource
import com.derekyu.testapp.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(
    localAppPageRepository: IAppLocalPageRepository,
    remoteAppPageRepository: IAppPageRepository,
    private val remoteAppRecommendationRepository: IAppRecommendationRepository
) : ViewModel() {
    private var mergedPageSource: AppPageMergedPagingSource? = null
    private val appPage = Pager(
        PagingConfig(
            pageSize = Constants.Paging.PAGE_LOAD_SIZE,
            prefetchDistance = 1,
            enablePlaceholders = true,
            initialLoadSize = 1
        )
    ) {
        AppPageMergedPagingSource(
            localAppPageRepository,
            remoteAppPageRepository,
            isQuerying
        ).apply {
            mergedPageSource = this
        }
    }.flow.cachedIn(viewModelScope)
    private val _appPageLoadState: MutableLiveData<MyLoadState<PagingData<AppInfoDTO>>> =
        MutableLiveData()
    val appPageLoadState: LiveData<MyLoadState<PagingData<AppInfoDTO>>>
        get() = _appPageLoadState
    private val _appRecommendationLoadState: MutableLiveData<MyLoadState<List<AppInfoDTO>>> =
        MutableLiveData()
    val appRecommendationLoadState: LiveData<MyLoadState<List<AppInfoDTO>>>
        get() = _appRecommendationLoadState
    private val appRecommendations: MutableList<AppInfoDTO> = mutableListOf()
    var isQuerying: Boolean = false

    init {
        _appPageLoadState.postValue(MyLoadState.Loading())

        viewModelScope.launch {
            appPage.collectLatest {
                _appPageLoadState.postValue(MyLoadState.Success(it))
            }
        }

        fetchAppRecommendationList()
    }

    fun fetchAppRecommendationList() {
        _appRecommendationLoadState.postValue(MyLoadState.Loading())
        viewModelScope.launch {
            try {
                remoteAppRecommendationRepository.loadRecommendation()?.let {
                    appRecommendations.clear()
                    appRecommendations.addAll(it)
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
        appPage.retry()
    }

    fun startQuery(
        query: String?
    ) {
        val trimmed = query?.trim()
        isQuerying = !trimmed.isNullOrBlank()
        queryAppPage(trimmed)
        queryAppRecommendation(trimmed)
    }

    private fun queryAppPage(query: String?) {
        if (_appPageLoadState.value == null || _appPageLoadState.value is MyLoadState.Fail) return

        mergedPageSource?.isLoadMoreDisabled = isQuerying
        if (isQuerying) {
            viewModelScope.launch {
                appPage.collectLatest {
                    _appPageLoadState.postValue(
                        MyLoadState.Success(
                            it.filter { dto ->
                                dto.matchQuery(query!!)
                            })
                    )
                }
            }
        } else {
            viewModelScope.launch {
                appPage.collectLatest {
                    _appPageLoadState.postValue(
                        MyLoadState.Success(it)
                    )
                }
            }
        }
    }

    private fun queryAppRecommendation(query: String?) {
        if (appRecommendations.isEmpty()) return

        if (isQuerying) {
            appRecommendations.filter {
                it.matchQuery(query!!)
            }.let {
                _appRecommendationLoadState.postValue(MyLoadState.Success(it))
            }
        } else {
            _appRecommendationLoadState.postValue(MyLoadState.Success(appRecommendations))
        }
    }
}

/**
 * Factory for [MainViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {
    private val iAppRemoteDataSource by lazy { AppRemoteDataSource(RetrofitBuilder.APP_REMOTE_SERVICE) }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(
            AppPageLocalRepository(),
            AppPageRemoteRepository(iAppRemoteDataSource),
            AppRecommendationRemoteRepository(iAppRemoteDataSource)
        ) as T
    }
}