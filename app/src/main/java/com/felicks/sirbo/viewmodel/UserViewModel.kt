package com.felicks.sirbo.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicks.sirbo.data.local.entity.toDomain
import com.felicks.sirbo.data.repository.UserRepository
import com.felicks.sirbo.domain.models.User
import com.felicks.sirbo.domain.models.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository, // tú capa de datos
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = "UserViewModel";
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        viewModelScope.launch {
            val storedUser = userRepository.getCurrentUser()
            Log.d(TAG, "uSUARIO STORED: $storedUser")
            if (storedUser != null) {
                _user.value = storedUser
            } else {
                val deviceId = userRepository.getOrCreateDeviceId(context)
                val guestUser = userRepository.initGuestUser(deviceId)
                _user.value = guestUser.toDomain()
            }
        }
    }

    fun signIn(user: User) {
        _user.value = user
        viewModelScope.launch {
            userRepository.saveUser(user.toEntity())
        }
    }

    fun signOut() {
        _user.value = null
        viewModelScope.launch {
            userRepository.clearUserSession()
        }
    }

    fun isLoggedIn(): Boolean = _user.value != null && !_user.value!!.isGuest
}
