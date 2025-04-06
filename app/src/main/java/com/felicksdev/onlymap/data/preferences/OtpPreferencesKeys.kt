package com.felicksdev.onlymap.data.preferences


import androidx.datastore.preferences.core.*

object OtpPreferenceKeys {
    val MODE = stringPreferencesKey("mode")
    val WALK_DISTANCE = intPreferencesKey("walk_distance")
    val MAX_TRANSFERS = intPreferencesKey("max_transfers")
    val NUM_ITINERARIES = intPreferencesKey("num_itineraries")
}

