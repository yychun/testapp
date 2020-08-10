package com.derekyu.testapp.data.repository.inmemory

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import com.derekyu.testapp.data.IAppDataSource
import com.derekyu.testapp.data.model.AppInfo
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.AppInfoFeed
import retrofit2.HttpException
import java.io.IOException

class PageKeyedPagingSource(
    private val appDataSource: IAppDataSource
) : PagingSource<Int, AppInfoDTO>() {
    private var appInfoFeed: AppInfoFeed? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppInfoDTO> {
        return try {
            if (appInfoFeed == null) {
                retrieveAppInfoList()
            }
            val page = params.key ?: INITIAL_PAGE
            val start = page * PAGE_SIZE
            sublist(start)?.mapNotNull { appInfo ->
                retrieveAppLookup(appInfo.appID.attributes.id).results.firstOrNull()?.let {
                    AppInfoDTO(appInfo, it)
                }
            }.let {
                val prevKey = if (page == INITIAL_PAGE) null else page - 1
                val nextKey = it?.let { page + 1 }
                Page(
                    data = it ?: listOf(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    private suspend fun retrieveAppInfoList() {
        appInfoFeed = appDataSource.retrieveTopFreeApps(TOTAL_SIZE).feed
    }

    private fun sublist(start: Int): List<AppInfo>? =
        appInfoFeed?.entry?.takeIf { it.size > start }?.subList(start, start + PAGE_SIZE)

    private suspend fun retrieveAppLookup(appID: String) = appDataSource.retrieveAppLookup(appID)

    companion object {
        const val INITIAL_PAGE = 0
        const val PAGE_SIZE = 10
        const val TOTAL_SIZE = 100
    }
}
