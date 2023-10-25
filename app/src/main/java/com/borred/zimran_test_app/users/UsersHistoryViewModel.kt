package com.borred.zimran_test_app.users

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borred.ktor_client.network.search.users.UsersHistoryDataStore
import com.borred.zimran_test_app.safeLaunch
import com.borred.zimran_test_app.users.model.GitUserUI
import com.borred.zimran_test_app.users.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class UsersHistoryViewModel
@Inject constructor(
    private val dataStore: UsersHistoryDataStore
) : ViewModel() {

    private val _historyListState: MutableState<HistoryState> = mutableStateOf(HistoryState.Loading)
    val historyListState: State<HistoryState> = _historyListState

    init {
        viewModelScope.safeLaunch {
            dataStore.getSearchHistoryFlow().collect { users ->
                if (users.isEmpty()) {
                    _historyListState.value = HistoryState.Empty
                    return@collect
                }
                val lastTwenty = users.takeLast(20).map { it.toUI() }
                _historyListState.value = HistoryState.List(lastTwenty.toImmutableList())
            }
        }
    }

    fun deleteHistory() {
        viewModelScope.safeLaunch {
            dataStore.delete()
            _historyListState.value = HistoryState.Empty
        }
    }
}

@Stable
sealed interface HistoryState {

    object Loading : HistoryState

    data class List(
        val list: ImmutableList<GitUserUI>
    ) : HistoryState

    object Empty : HistoryState
}
