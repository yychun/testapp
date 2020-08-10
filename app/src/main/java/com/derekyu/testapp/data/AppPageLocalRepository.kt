package com.derekyu.testapp.data

import com.derekyu.testapp.data.model.AppInfoDTO
import com.derekyu.testapp.data.repository.IAppPageLocalRepository

class AppPageLocalRepository : IAppPageLocalRepository {
    private val dataMap: MutableMap<Int, List<AppInfoDTO>> = hashMapOf()

    override suspend fun loadNextPage(page: Int): List<AppInfoDTO>? = dataMap[page]

    override fun insertData(page: Int, data: List<AppInfoDTO>) {
        dataMap[page] = data
    }
}