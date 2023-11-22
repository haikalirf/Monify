package com.bleh.monify.feature_more.more

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import com.bleh.monify.feature_auth.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MoreState(
    val profileEmail: String = "testemail@gmail.com",
    val profileImage: Int = R.drawable.default_profile_image,
)

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val authUiClient: GoogleAuthUiClient
): ViewModel() {
    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

    fun updateProfileEmailState (name: String) {
        _state.update {
            it.copy(profileEmail = name)
        }
    }

    fun updateProfileImageState (image: Int) {
        _state.update {
            it.copy(profileImage = image)
        }
    }

    suspend fun onLogoutClick() {
        authUiClient.logout()
    }
}