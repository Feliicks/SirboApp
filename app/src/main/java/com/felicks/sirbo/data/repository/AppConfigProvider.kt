package com.felicks.sirbo.data.repository

import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AppConfigProvider @Inject constructor(
    private val repository: AppConfigRepository
) {
    fun getBaseUrl(): String {
        return runBlocking {
            repository.getBaseUrl() ?: "http://10.0.2.2"
        }
    }
}
