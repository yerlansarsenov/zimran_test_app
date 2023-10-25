package com.borred.zimran_test_app.repodetails

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.users.UsersHistoryDataStore
import com.borred.zimran_test_app.error.ErrorsFlow
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.safeLaunch
import com.borred.zimran_test_app.ui.SpecialCharsMap
import com.borred.zimran_test_app.users.model.GitUserUI
import com.borred.zimran_test_app.users.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val repositoryApi: SearchRepositoryApi,
    savedStateHandle: SavedStateHandle,
    errorsFlow: ErrorsFlow,
    private val usersHistoryDataStore: UsersHistoryDataStore
) : ViewModel() {

    private val repoJson = savedStateHandle.get<String>("repo") ?: ""
    val gitRepository = repoJson.let {
        Log.e("HERE!!", "viewModel: it = $it")
        var str = it
        SpecialCharsMap.forEach { (value, key) ->
            str = str.replace(value, key)
        }
        Json.decodeFromString<GitRepositoryUI>(str)
    }

    private val _langsState = mutableStateOf<LangState>(LangState.Loading)
    val langsState: State<LangState> = _langsState

    init {
        viewModelScope.safeLaunch {
            repositoryApi.getLanguagesOfRepo(
                login = gitRepository.owner.login,
                repoName = gitRepository.name
            ).onFailure {
                _langsState.value = LangState.Empty
                errorsFlow.sendError(it)
            }.onSuccess {
                Log.e("HERE!!", "viewModel: it = $it")
                if (it.isEmpty()) {
                    _langsState.value = LangState.Empty
                    return@safeLaunch
                }
                _langsState.value = LangState.Loaded(
                    it.toImmutableMap()
                )
            }
        }
    }

    fun addUserToHistory(user: GitUserUI) {
        viewModelScope.safeLaunch {
            usersHistoryDataStore.addToHistory(user.toDomain())
        }
    }
}

@Stable
sealed interface LangState {

    data class Loaded(
        val map: ImmutableMap<String, Int>
    ) : LangState

    object Loading : LangState

    object Empty : LangState
}
