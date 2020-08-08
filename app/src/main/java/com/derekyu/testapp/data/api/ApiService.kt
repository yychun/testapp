package com.derekyu.testapp.data.api

import com.derekyu.testapp.data.model.AppInfoList
import com.derekyu.testapp.data.model.AppLookup
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/rss/topfreeapplications/limit={limit}/json")
    suspend fun retrieveAppInfoList(@Path("limit") limit: Int): AppInfoList

    @GET("/lookup?id={app_id}")
    suspend fun retrieveAppLookup(@Path("app_id") appID: String): AppLookup
}