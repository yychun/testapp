package com.derekyu.testapp.data.api

import com.derekyu.testapp.data.IAppDataSource

class AppRemoteDataSource(
    private val apiService: ApiService
) : IAppDataSource {
    override suspend fun retrieveTopFreeApps(limit: Int) = apiService.retrieveTopFreeApps(limit)
    override suspend fun retrieveTopGrossingApps(limit: Int) =
        apiService.retrieveTopGrossingApps(limit)

    override suspend fun retrieveAppLookup(appID: String) = apiService.retrieveAppLookup(appID)
}