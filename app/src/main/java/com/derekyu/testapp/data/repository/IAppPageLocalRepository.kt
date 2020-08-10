package com.derekyu.testapp.data.repository

import com.derekyu.testapp.data.model.AppInfoDTO

interface IAppPageLocalRepository : IAppPageRepository {
    fun insertData(page: Int, data: List<AppInfoDTO>)
}