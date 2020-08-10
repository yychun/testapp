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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    }.flow
    private val _appPageLoadState: MutableLiveData<MyLoadState<PagingData<AppInfoDTO>>> =
        MutableLiveData()
    val appPageLoadState: LiveData<MyLoadState<PagingData<AppInfoDTO>>>
        get() = _appPageLoadState

    private val _appRecommendationLoadState: MutableLiveData<MyLoadState<List<AppInfoDTO>>> =
        MutableLiveData()
    val appRecommendationLoadState: LiveData<MyLoadState<List<AppInfoDTO>>>
        get() = _appRecommendationLoadState
    var isQuerying: Boolean = false

    init {
        appPage.catch {
            when (it) {
                is IOException, is HttpException -> {
                    _appPageLoadState.postValue(MyLoadState.Fail(MyError.Network(it)))
                }
            }
        }

        viewModelScope.launch {
            appPage.collectLatest {
                _appPageLoadState.postValue(MyLoadState.Success(it))
            }
        }

        fetchAppRecommendationList()
    }

    fun fetchAppRecommendationList() {
        viewModelScope.launch {
            try {
                remoteAppRecommendationRepository.loadRecommendation()?.let {
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

    fun queryAppPage(
        query: String?
    ) {
        isQuerying = !query.isNullOrBlank()
        Log.d("Testing", "isQuerying: $isQuerying")
//        appPagePagingSource.isLoadMoreDisabled = isQuerying
        if (isQuerying) {
            GlobalScope.launch {
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
            GlobalScope.launch {
                appPage.collectLatest {
                    _appPageLoadState.postValue(
                        MyLoadState.Success(it)
                    )
                }
            }
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