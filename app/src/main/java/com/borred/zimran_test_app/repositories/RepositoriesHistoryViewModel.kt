package com.borred.zimran_test_app.repositories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borred.ktor_client.network.search.repos.RepositoriesHistoryDataStore
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.repositories.model.toUI
import com.borred.zimran_test_app.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class RepositoriesHistoryViewModel
@Inject constructor(
    private val dataStore: RepositoriesHistoryDataStore
) : ViewModel() {

    private val _historyListState: MutableState<HistoryState> = mutableStateOf(HistoryState.Loading)
    val historyListState: State<HistoryState> = _historyListState

    init {
        viewModelScope.safeLaunch {
            val list = dataStore.getSearchHistoryFlow().firstOrNull()
            if (list == null || list.isEmpty()) {
                _historyListState.value = HistoryState.Empty
                return@safeLaunch
            }
            val lastTwenty = list.takeLast(20).map { it.toUI() }
            _historyListState.value = HistoryState.List(lastTwenty.toImmutableList())
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
        val list: ImmutableList<GitRepositoryUI>
    ) : HistoryState

    object Empty : HistoryState
}
