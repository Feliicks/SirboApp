package com.felicks.sirbo.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "otp_config")
