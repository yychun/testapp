package com.derekyu.testapp.data.repository

import com.derekyu.testapp.data.model.AppInfoDTO

interface IAppLocalPageRepository : IAppPageRepository {
    fun insertData(page: Int, data: List<AppInfoDTO>)
}