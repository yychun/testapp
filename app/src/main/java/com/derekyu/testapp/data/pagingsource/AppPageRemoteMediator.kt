package com.derekyu.testapp.data.pagingsource

import androidx.paging.*
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.repository.IAppPageRepository

@ExperimentalPagingApi
class AppPageRemoteMediator(
    private val iAppPageRepository: IAppPageRepository,
    private val onRemoteDataFetched: ((List<AppInfoDTO>) -> Unit)
) : RemoteMediator<Int, AppInfoDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AppInfoDTO>
    ): MediatorResult {
        return when (loadType) {
            LoadType.APPEND, LoadType.REFRESH -> {
                val page = state.anchorPosition?.div(state.config.pageSize) ?: 0
                iAppPageRepository.loadNextPage(page)?.takeIf { it.isNotEmpty() }?.let {
                    onRemoteDataFetched(it)
                    MediatorResult.Success(false)
                } ?: kotlin.run {
                    MediatorResult.Success(true)
                }
            }
            LoadType.PREPEND -> {
                MediatorResult.Success(true)
            }
        }
    }
}