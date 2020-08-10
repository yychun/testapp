package com.derekyu.testapp.data.pagingsource

import androidx.paging.PagingSource
import com.derekyu.testapp.Constants
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.repository.IAppPageLocalRepository
import com.derekyu.testapp.data.repository.IAppPageRepository
import retrofit2.HttpException
import java.io.IOException

class AppPageMergedPagingSource(
    private val iAppPageLocalRepository: IAppPageLocalRepository,
    private val iAppPageRemoteRepository: IAppPageRepository,
    var isLoadMoreDisabled: Boolean = false,
    private val onLoadMoreWhenDisabled: ((key: Int) -> Unit)
) : PagingSource<Int, AppInfoDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppInfoDTO> {
        val key = params.key ?: Constants.Paging.INITIAL_PAGE
        var data = iAppPageLocalRepository.loadNextPage(key)
        if (data == null) {
            if (isLoadMoreDisabled) {
                onLoadMoreWhenDisabled.invoke(key)
                return LoadResult.Error<Int, AppInfoDTO>(Throwable("load more is disabled"))
            }
            try {
                data = iAppPageRemoteRepository.loadNextPage(key)?.apply {
                    iAppPageLocalRepository.insertData(key, this)
                }
            } catch (e: IOException) {
                return LoadResult.Error<Int, AppInfoDTO>(e)
            } catch (e: HttpException) {
                return LoadResult.Error<Int, AppInfoDTO>(e)
            }
        }
        return data.let {
            val prevKey = if (key == Constants.Paging.INITIAL_PAGE) null else key - 1
            val nextKey = it?.let { key + 1 }
            LoadResult.Page<Int, AppInfoDTO>(it ?: listOf(), prevKey, nextKey)
        }
    }
}