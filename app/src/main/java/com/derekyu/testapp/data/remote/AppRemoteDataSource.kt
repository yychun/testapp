package com.derekyu.testapp.data.remote

import com.derekyu.testapp.data.interfaces.IAppDataSource

class AppRemoteDataSource(
    private val appRemoteService: AppRemoteService
) : IAppDataSource {
    override suspend fun retrieveTopFreeApps(limit: Int) =
        appRemoteService.retrieveTopFreeApps(limit).feed

    override suspend fun retrieveTopGrossingApps(limit: Int) =
        appRemoteService.retrieveTopGrossingApps(limit).feed

    override suspend fun retrieveAppLookup(appID: String) =
        appRemoteService.retrieveAppLookup(appID).results
}