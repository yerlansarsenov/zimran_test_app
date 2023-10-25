package com.borred.ktor_client.network.search.users

import android.content.Context
import androidx.datastore.dataStore
import com.borred.ktor_client.network.search.users.model.GitUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersHistoryDataStore @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    serializer: UserSearchHistorySerializer
) {
    companion object {
        private const val SEARCH_HISTORY_DATA_STORE = "users_search_history_data_store.json"
    }

    private val Context.searchHistoryDataStore by dataStore<List<GitUser>>(
        fileName = SEARCH_HISTORY_DATA_STORE,
        serializer = serializer
    )

    private val searchHistoryFlow: Flow<List<GitUser>> = applicationContext.searchHistoryDataStore.data

    suspend fun addToHistory(user: GitUser) {
        val list = searchHistoryFlow.firstOrNull()?.toMutableList() ?: return
        list.add(user)
        setSearchHistory(list)
    }

    private suspend fun setSearchHistory(list: List<GitUser>) {
        applicationContext.searchHistoryDataStore.updateData {
            list.distinct()
        }
    }

    fun getSearchHistoryFlow(): Flow<List<GitUser>> = searchHistoryFlow

    suspend fun delete() {
        setSearchHistory(emptyList())
    }
}
