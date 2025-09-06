package com.felicks.sirbo.data.repository

import com.felicks.sirbo.data.local.dao.AppConfigDao
import com.felicks.sirbo.data.local.entity.AppConfigEntity
import com.felicks.sirbo.utils.UrlUtils
import javax.inject.Inject

class AppConfigRepository @Inject constructor(
    private val dao: AppConfigDao
) {
    suspend fun getBaseUrl(): String? {
        val rawUrl = dao.getConfig("base_url")?.value ?: "http://10.0.2.2/"
        return UrlUtils.normalizeBaseUrl(rawUrl)
    }

    suspend fun saveBaseUrl(url: String) {
        val normalizedUrl = UrlUtils.normalizeBaseUrl(url)
        dao.insertConfig(AppConfigEntity(key = "base_url", value = normalizedUrl))
    }
}
