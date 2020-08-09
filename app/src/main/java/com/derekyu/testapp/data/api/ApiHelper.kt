package com.derekyu.testapp.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun retrieveTopFreeApps(limit: Int) = apiService.retrieveTopFreeApps(limit)
    suspend fun retrieveTopGrossingApps(limit: Int) = apiService.retrieveTopGrossingApps(limit)
    suspend fun retrieveAppLookup(appID: String) = apiService.retrieveAppLookup(appID)
}