package com.derekyu.testapp.data.interfaces

import com.derekyu.testapp.data.model.AppInfoFeed
import com.derekyu.testapp.data.model.AppLookupResult

interface IAppDataSource {
    suspend fun retrieveTopFreeApps(limit: Int): AppInfoFeed
    suspend fun retrieveTopGrossingApps(limit: Int): AppInfoFeed
    suspend fun retrieveAppLookup(appID: String): List<AppLookupResult>
}