package com.derekyu.testapp.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun retrieveAppInfoList(limit: Int) = apiService.retrieveAppInfoList(limit)
    suspend fun retrieveAppLookup(appID: String) = apiService.retrieveAppLookup(appID)
}