package com.derekyu.testapp.data.repository

import com.derekyu.testapp.data.model.AppInfoDTO

interface IAppPageRepository {
    suspend fun loadNextPage(page: Int): List<AppInfoDTO>?
}