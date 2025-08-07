package com.felicks.sirbo.data.repository

import com.felicks.sirbo.data.local.dao.AppConfigDao
import com.felicks.sirbo.data.local.entity.AppConfigEntity
import javax.inject.Inject

class AppConfigRepository @Inject constructor(
    private val dao: AppConfigDao
) {
    suspend fun getBaseUrl(): String? {
        return dao.getConfig("base_url")?.value
    }

    suspend fun saveBaseUrl(url: String) {
        dao.insertConfig(AppConfigEntity(key = "base_url", value = url))
    }
}
