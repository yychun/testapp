package com.derekyu.testapp.data.pagingsource.local

import androidx.paging.PagingSource
import com.derekyu.testapp.Constants
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.repository.IAppLocalPageRepository
import com.derekyu.testapp.data.repository.IAppPageRepository

class AppPageMergedPagingSource(
    private val iAppLocalPageRepository: IAppLocalPageRepository,
    private val iAppRemotePageRepository: IAppPageRepository,
    var isLoadMoreDisabled: Boolean = false
) : PagingSource<Int, AppInfoDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppInfoDTO> {
        val key = params.key ?: Constants.Paging.INITIAL_PAGE
        var data = iAppLocalPageRepository.loadNextPage(key)
        if (data == null && !isLoadMoreDisabled) {
            data = iAppRemotePageRepository.loadNextPage(key)?.apply {
                iAppLocalPageRepository.insertData(key, this)
            }
        }
        return data.let {
            val prevKey = if (key == Constants.Paging.INITIAL_PAGE) null else key - 1
            val nextKey = it?.let { key + 1 }
            LoadResult.Page<Int, AppInfoDTO>(it ?: listOf(), prevKey, nextKey)
        }
    }
}