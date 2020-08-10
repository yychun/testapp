package com.derekyu.testapp.data.api

import com.derekyu.testapp.data.model.RetrieveAppLookupResponse
import com.derekyu.testapp.data.model.RetrieveAppsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/rss/topfreeapplications/limit={limit}/json")
    suspend fun retrieveTopFreeApps(@Path("limit") limit: Int): RetrieveAppsResponse

    @GET("/rss/topgrossingapplications/limit={limit}/json")
    suspend fun retrieveTopGrossingApps(@Path("limit") limit: Int): RetrieveAppsResponse

    @GET("/lookup")
    suspend fun retrieveAppLookup(@Query("id") appID: String): RetrieveAppLookupResponse
}