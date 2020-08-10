package com.derekyu.testapp.data.repository

import androidx.annotation.NonNull
import com.derekyu.testapp.Constants
import com.derekyu.testapp.data.interfaces.IAppDataSource
import com.derekyu.testapp.data.model.AppInfo
import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.model.AppInfoFeed

class AppPageRemoteRepository(
    private val iAppDataSource: IAppDataSource
): IAppPageRepository {
    private var appInfoFeed: AppInfoFeed? = null

    override suspend fun loadNextPage(@NonNull page: Int): List<AppInfoDTO>? {
        val start = page * Constants.Paging.PAGE_LOAD_SIZE

        if (appInfoFeed == null) {
            retrieveAppInfoList()
        }
        return sublist(start)?.mapNotNull { appInfo ->
            retrieveAppLookup(appInfo.appID.attributes.id).firstOrNull()?.let {
                AppInfoDTO(appInfo, it)
            }
        }
    }

    private suspend fun retrieveAppInfoList() {
        appInfoFeed = iAppDataSource.retrieveTopFreeApps(Constants.Paging.TOP_FREE_APP_SIZE)
    }

    private fun sublist(start: Int): List<AppInfo>? =
        appInfoFeed?.entry?.takeIf { it.size > start }?.subList(start, start + Constants.Paging.PAGE_LOAD_SIZE)

    private suspend fun retrieveAppLookup(appID: String) = iAppDataSource.retrieveAppLookup(appID)
}