package com.borred.zimran_test_app.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borred.ktor_client.local.AccessTokenDataStore
import com.borred.ktor_client.network.GithubAuthApi
import com.borred.zimran_test_app.error.ErrorsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ScreenState {
    // splashScreen
    object Loading : ScreenState

    object NotAuthorized : ScreenState

    object Main : ScreenState
}

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val githubAuthApi: GithubAuthApi,
    private val errorsFlow: ErrorsFlow,
    private val accessTokenDataStore: AccessTokenDataStore
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            val accessToken = accessTokenDataStore.getAccessTokenFlow().firstOrNull()
            if (accessToken == null) {
                _screenState.emit(ScreenState.NotAuthorized)
            } else {
                _screenState.emit(ScreenState.Main)
            }
        }
    }

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            _screenState.emit(ScreenState.Loading)
            githubAuthApi.getAccessToken(
                code = code
            ).onFailure {
                it.printStackTrace()
                errorsFlow.sendError(it)
                _screenState.emit(ScreenState.NotAuthorized)
            }.onSuccess { token ->
                accessTokenDataStore.setAccessToken(token)
                _screenState.emit(ScreenState.Main)
            }
        }
    }
}
