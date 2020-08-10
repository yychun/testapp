package com.derekyu.testapp.data

import com.derekyu.testapp.data.model.RetrieveAppLookupResponse
import com.derekyu.testapp.data.model.RetrieveAppsResponse

interface IAppDataSource {
    suspend fun retrieveTopFreeApps(limit: Int): RetrieveAppsResponse
    suspend fun retrieveTopGrossingApps(limit: Int): RetrieveAppsResponse
    suspend fun retrieveAppLookup(appID: String): RetrieveAppLookupResponse
}