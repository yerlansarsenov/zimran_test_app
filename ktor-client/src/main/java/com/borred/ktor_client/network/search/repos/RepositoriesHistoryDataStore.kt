package com.borred.ktor_client.network.search.repos

import android.content.Context
import androidx.datastore.dataStore
import com.borred.ktor_client.network.search.repos.model.GitRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoriesHistoryDataStore @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    serializer: RepoSearchHistorySerializer
) {
    companion object {
        private const val SEARCH_HISTORY_DATA_STORE = "repo_search_history_data_store.json"
    }

    private val Context.searchHistoryDataStore by dataStore<List<GitRepository>>(
        fileName = SEARCH_HISTORY_DATA_STORE,
        serializer = serializer
    )

    private val searchHistoryFlow: Flow<List<GitRepository>> = applicationContext.searchHistoryDataStore.data

    suspend fun addToHistory(repo: GitRepository) {
        val list = searchHistoryFlow.firstOrNull()?.toMutableList() ?: return
        list.add(repo)
        setSearchHistory(list)
    }

    private suspend fun setSearchHistory(list: List<GitRepository>) {
        applicationContext.searchHistoryDataStore.updateData {
            list
        }
    }

    fun getSearchHistoryFlow(): Flow<List<GitRepository>> = searchHistoryFlow
}
