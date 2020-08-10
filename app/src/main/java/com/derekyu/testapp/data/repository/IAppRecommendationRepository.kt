package com.derekyu.testapp.data.repository

import com.derekyu.testapp.data.model.AppInfoDTO

interface IAppRecommendationRepository {
    suspend fun loadRecommendation(): List<AppInfoDTO>?
}