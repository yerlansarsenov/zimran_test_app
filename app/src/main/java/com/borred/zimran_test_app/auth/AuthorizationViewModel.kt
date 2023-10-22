package com.borred.zimran_test_app.auth

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

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val githubAuthApi: GithubAuthApi,
    private val errorsFlow: ErrorsFlow,
    private val accessTokenDataStore: AccessTokenDataStore
) : ViewModel() {

    private val _screenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            val accessToken = accessTokenDataStore.getAccessTokenFlow().firstOrNull()
            if (accessToken == null) {
                _screenState.emit(AuthScreenState.NotAuthorized)
            } else {
                _screenState.emit(AuthScreenState.Main)
            }
        }
    }

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            _screenState.emit(AuthScreenState.Loading)
            githubAuthApi.getAccessToken(
                code = code
            ).onFailure {
                it.printStackTrace()
                errorsFlow.sendError(it)
                _screenState.emit(AuthScreenState.NotAuthorized)
            }.onSuccess { token ->
                accessTokenDataStore.setAccessToken(token)
                _screenState.emit(AuthScreenState.Main)
            }
        }
    }
}
