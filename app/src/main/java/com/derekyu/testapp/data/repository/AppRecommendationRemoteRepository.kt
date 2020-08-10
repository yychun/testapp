package com.derekyu.testapp.data.repository

import com.derekyu.testapp.Constants
import com.derekyu.testapp.data.interfaces.IAppDataSource
import com.derekyu.testapp.data.model.AppInfoDTO

class AppRecommendationRemoteRepository(
    private val iAppDataSource: IAppDataSource
) : IAppRecommendationRepository {
    override suspend fun loadRecommendation(): List<AppInfoDTO>? =
        iAppDataSource.retrieveTopGrossingApps(Constants.Paging.TOP_RECOMMENDATION_APP_SIZE).entry.map {
            AppInfoDTO(
                it
            )
        }
}